package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.model.DevAccount;

import org.springframework.jdbc.core.RowMapper;

@Component
public class DevAccountRowMapper implements RowMapper<DevAccount> {

    @Override
	public DevAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
        DevAccount u = new DevAccount();
        u.setId(rs.getLong("account_id"));
        u.setEmail(rs.getString("email"));
        u.setOtp(rs.getString("otp"));
        u.setVerified(rs.getBoolean("verified"));
        u.setPasswordHash(rs.getString("password"));
        u.setUnityInvoice(rs.getString("unity_invoice"));
		return u;
	}

}