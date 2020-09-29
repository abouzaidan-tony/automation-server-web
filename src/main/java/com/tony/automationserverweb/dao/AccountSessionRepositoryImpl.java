package com.tony.automationserverweb.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.tony.automationserverweb.model.AccountSession;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.mapper.AccountSessionRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;


@Repository
public class AccountSessionRepositoryImpl implements IRepository<AccountSession, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired 
    private AccountSessionRowMapper accountSessionRowMapper;

    private static final String deleteQuery = "DELETE FROM account_session WHERE id = ?";
    private static final String deleteAccIdQuery = "DELETE FROM account_session WHERE account_id = ? and account_type = ?";
    private static final String insertQuery = "INSERT INTO account_session (account_id, account_type, token, expiry_date, application_token) VALUES (?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE account_session SET account_id = ?, account_type = ?, token = ?, expiry_date = ?, application_token = ? WHERE id = ?";
    private static final String selectQuery = "SELECT id, account_id, account_type, token, expiry_date, application_token FROM account_session";
    private static final String uniqueTokenQuery = "SELECT COUNT(*) FROM account_session WHERE token = ?";

    @Override
    public AccountSession insert(AccountSession object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, object.getAccountId());
            ps.setString(2, object.getAccType());
            ps.setString(3, object.getToken());
            ps.setDate(4, new Date(object.getExpiryDate().getTime()));
            ps.setString(5, object.getApplicationToken());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        return object;
    }

    @Override
    public AccountSession update(AccountSession object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setLong(1, object.getAccountId());
            ps.setString(2, object.getAccType());
            ps.setString(3, object.getToken());
            ps.setDate(4, new Date(object.getExpiryDate().getTime()));
            ps.setString(5, object.getApplicationToken());
            ps.setLong(6, object.getId());
            return ps;
        });
        return object;
    }

    @Override
    public AccountSession findOneById(Long id) {
        return jdbcTemplate.queryForObject(selectQuery.concat(" WHERE id = ?"), new Object[]{id}, 
                accountSessionRowMapper);
    }

    @Override
    public List<AccountSession> getAll() {
        return jdbcTemplate.query(selectQuery, accountSessionRowMapper);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDeviceRowMapper(AccountSessionRowMapper accountSessionRowMapper){
        this.accountSessionRowMapper = accountSessionRowMapper;
    }

    public void delete(AccountSession device){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setLong(1, device.getId());
            return ps;
        });
    }

    public void deleteByAccountId(long accountId, String type) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(deleteAccIdQuery);
            ps.setLong(1, accountId);
            ps.setString(2, type);
            return ps;
        });
    }

    public AccountSession findOneByToken(String token) {
        try {
        return jdbcTemplate.queryForObject(selectQuery.concat(" WHERE token = ?"), new Object[] { token },
                accountSessionRowMapper);
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    public String generateUniqueToken(String type) {
        int count = 0;
        String token = null;
        do {
            token = type + Helper.generateToken().concat(Helper.generateToken()).concat(Helper.generateToken());
            count = jdbcTemplate.queryForObject(uniqueTokenQuery, new Object[] { token }, Integer.class);
        } while (count != 0);
        return token;
    }
}