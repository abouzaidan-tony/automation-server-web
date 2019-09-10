package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.dao.DevAccountRepositoryImpl;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.DevAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

@Component
public class ApplicationRowMapper implements RowMapper<Application> {

    @Autowired
    private DevAccountRepositoryImpl devAccountRepository;

    private DevAccount account;

    @Override
	public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
        Application d = new Application();
        d.setId(rs.getLong("app_id"));
        d.setToken(rs.getString("app_token"));
        Long id = rs.getLong("account_id");
        if(account != null)
            d.setAccount(account);
        else if(!rs.wasNull())
            d.setAccount(devAccountRepository.findOneById(id));
		return d;
    }
    
    public void setParentAccount(DevAccount account){
        this.account = account;
    }
}