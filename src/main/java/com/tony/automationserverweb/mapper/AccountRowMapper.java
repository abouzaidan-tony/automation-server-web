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
        u.setQ1(rs.getInt("q1"));
        u.setQ2(rs.getInt("q2"));
        u.setAnswer1(rs.getString("answer1"));
        u.setAnswer2(rs.getString("answer2"));
		return u;
	}

}