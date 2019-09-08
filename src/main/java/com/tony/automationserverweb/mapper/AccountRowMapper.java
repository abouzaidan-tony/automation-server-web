package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.model.Account;

import org.springframework.jdbc.core.RowMapper;

@Component
public class AccountRowMapper implements RowMapper<Account> {

    @Override
	public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account u = new Account();
        u.setId(rs.getLong("account_id"));
        u.setNickname(rs.getString("nickname"));
        u.setEmail(rs.getString("email"));
        u.setOtp(rs.getString("otp"));
        u.setPasswordHash(rs.getString("password"));
        u.setToken(rs.getString("token"));
		return u;
	}

}