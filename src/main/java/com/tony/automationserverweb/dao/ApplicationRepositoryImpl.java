package com.tony.automationserverweb.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.mapper.ApplicationRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ApplicationRepositoryImpl implements IRepository<Application, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApplicationRowMapper applicationRowMapper;

    private static final String insertQuery = "INSERT INTO application (app_name, app_token, dev_account_id) VALUES (?, ?, ?)";
    private static final String updateQuery = "UPDATE application SET app_name = ?, app_token = ?, dev_account_id = ?, WHERE id = ?";
    private static final String selectQuery = "SELECT application.id app_id, app_token, app_name, dev_account_id FROM application";
    private static final String deleteQuery = "DELETE FROM application WHERE id = ?";
    private static final String uniqueTokenQuery = "SELECT COUNT(*) FROM application WHERE app_token = ?";

    @Override
    @Transactional
    public Application insert(Application object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, object.getName());
            ps.setString(2, object.getToken());
            ps.setLong(3, object.getAccountId());
            return ps;
        }, keyHolder);

        object.setId(keyHolder.getKey().longValue());

        return object;
    }

    @Override
    public Application update(Application object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, object.getName());
            ps.setString(2, object.getToken());
            ps.setLong(3, object.getAccountId());
            ps.setLong(4, object.getId());
            return ps;
        });
        return object;
    }

    @Override
    public Application findOneById(Long id) {
        List<Application> users = getAllByQuery(selectQuery.concat(" WHERE application.id = ?"), new Object[] {id});
        if(users.size() == 0)
            return null;
        return users.get(0);
    }

    public Application findOneByToken(String token) {
        List<Application> users = getAllByQuery(selectQuery.concat(" WHERE app_token = ?"), new Object[] { token});
        if(users.size() == 0)
            return null;
        return users.get(0);
    }

    @Override
    public List<Application> getAll() {
        return getAllByQuery(selectQuery);
    }

    private List<Application> getAllByQuery(String query){
        return jdbcTemplate.query(query, applicationRowMapper);
    }

    private List<Application> getAllByQuery(String query, Object [] args){
        return jdbcTemplate.query(query, args, applicationRowMapper);
    }
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateUniqueToken(){
        int count = 0;
        String token = null;
        do{
            token = Helper.generateToken();
            count = jdbcTemplate.queryForObject(uniqueTokenQuery, new Object[]{token}, Integer.class);
        }while(count != 0);
        return token;
    }

    public void delete(Application application) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setLong(1, application.getId());
            return ps;
        });
    }
}