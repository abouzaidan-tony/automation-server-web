package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.model.AccountSession;
import org.springframework.jdbc.core.RowMapper;

@Component
public class AccountSessionRowMapper implements RowMapper<AccountSession> {

    @Override
    public AccountSession mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountSession session = new AccountSession();
        session.setId(rs.getLong("id"));
        session.setAccountId(rs.getLong("account_id"));
        session.setAccType(rs.getString("account_type"));
        session.setToken(rs.getString("token"));
        session.setExpiryDate(rs.getDate("expiry_date"));
        session.setApplicationToken(rs.getString("application_token"));
        return session;
    }
}