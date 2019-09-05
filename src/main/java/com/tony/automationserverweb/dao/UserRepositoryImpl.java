package com.tony.automationserverweb.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.tony.automationserverweb.model.User;
import com.tony.automationserverweb.mapper.UserRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements IRepository<User, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired 
    private UserRowMapper userRowMapper;

    private static final String deleteQuery = "DELETE FROM user WHERE id = ?";
    private static final String insertQuery = "INSERT INTO user (user_key, account_id, connected) VALUES (?, ?, 0)";
    private static final String updateQuery = "UPDATE user SET user_key = ?, account_id = ? WHERE id = ?";
    private static final String selectQuery = "SELECT id user_id, user_key, connected u_connected, account_id FROM user";

    @Override
    public User insert(User object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, object.getKey());
            ps.setLong(2, object.getAccountId());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        return object;
    }

    @Override
    public User update(User object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, object.getKey());
            ps.setLong(2, object.getAccountId());
            return ps;
        });
        return object;
    }

    @Override
    public User findOneById(Long id) {
        return jdbcTemplate.queryForObject(selectQuery.concat(" WHERE id = ?"), new Object[]{id}, userRowMapper);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(selectQuery, userRowMapper);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDeviceRowMapper(UserRowMapper userRowMapper){
        this.userRowMapper = userRowMapper;
    }

    public void delete(User device){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setLong(1, device.getId());
            return ps;
        });
    }
}