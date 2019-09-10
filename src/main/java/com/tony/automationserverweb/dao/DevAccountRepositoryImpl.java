package com.tony.automationserverweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.DevAccount;
import com.tony.automationserverweb.mapper.ApplicationRowMapper;
import com.tony.automationserverweb.mapper.DevAccountRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DevAccountRepositoryImpl implements IRepository<DevAccount, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApplicationRepositoryImpl applicationRepositoryImpl;

    @Autowired
    private DevAccountRowMapper devAccountRowMapper;

    @Autowired
    private ApplicationRowMapper applicationRowMapper;

    private static final String countQuery = "SELECT count(*) FROM dev_account WHERE email = ?";
    private static final String insertQuery = "INSERT INTO dev_account (email, password, otp) VALUES (?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE dev_account SET email = ?, password = ?, otp = ? WHERE id = ?";
    private static final String selectQuery = "SELECT dev_account.id account_id, email, password, otp, application.id app_id, app_name, app_token FROM dev_account LEFT JOIN application ON application.account_id = dev_account.id ";

    @Override
    @Transactional
    public DevAccount insert(DevAccount object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, object.getEmail());
            ps.setString(2, object.getPasswordHash());
            ps.setString(3, object.getOtp());
            return ps;
        }, keyHolder);

        object.setId(keyHolder.getKey().longValue());

        for (Application d : object.getApplicatons()) {
            d.setAccount(object);
            applicationRepositoryImpl.insert(d);
        }
    
        return object;
    }

    @Override
    public DevAccount update(DevAccount object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, object.getEmail());
            ps.setString(2, object.getPasswordHash());
            ps.setString(3, object.getOtp());
            ps.setLong(4, object.getId());
            return ps;
        });
        return object;
    }

    @Override
    public DevAccount findOneById(Long id) {
        List<DevAccount> accounts = getAllByQuery(selectQuery.concat(" WHERE dev_account.id = " + id));
        return accounts.get(0);
    }

    @Override
    public List<DevAccount> getAll() {
        return getAllByQuery(selectQuery);
    }

    private List<DevAccount> getAllByQuery(String query){
        return jdbcTemplate.query(query, new DevAccountListSetExtractor());
    }
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DevAccount getDevAccountByEmail(String email){
        List<DevAccount> list = jdbcTemplate.query(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(selectQuery.concat(" WHERE email = ?"));
                ps.setString(1, email);
                return ps;
            }
        }, new DevAccountListSetExtractor());

        if(list.size() == 0)
            return null;
        return list.get(0);
    }


    class DevAccountListSetExtractor implements ResultSetExtractor<List<DevAccount>> {

        @Override
        public List<DevAccount> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<DevAccount> users = new ArrayList<DevAccount>();
            DevAccount account = null;
            int usrIdx = 0;
            int deviceIdx = 0;
            Long deviceId = null;
            while (rs.next()) {
                if (account == null || account.getId() != rs.getLong("account_id")) {
                    account = devAccountRowMapper.mapRow(rs, usrIdx++);
                    applicationRowMapper.setParentAccount(account);
                    users.add(account);
                }

                Long a = rs.getLong("device_id");
                if (!rs.wasNull() && deviceId != a)
                {
                    deviceId = a;
                    Application d = applicationRowMapper.mapRow(rs, deviceIdx++);
                    account.getApplicatons().add(d);
                }
            }
            return users;
        }
        
    }

    public Integer getCountAccountsByEmail(String email){
        return jdbcTemplate.queryForObject(countQuery, new Object[] {email}, Integer.class);
    }
}