package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

@Component
public class DeviceRowMapper implements RowMapper<Device> {

    @Autowired
    private AccountRepositoryImpl accountRepository;

    private Account account;

    @Override
	public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
        Device d = new Device();
        d.setId(rs.getLong("device_id"));
        d.setKey(rs.getString("device_key"));
        d.setConnected(rs.getBoolean("d_connected"));
        Long id = rs.getLong("account_id");
        if(account != null)
            d.setAccount(account);
        else if(!rs.wasNull())
            d.setAccount(accountRepository.findOneById(id));
		return d;
    }
    
    public void setParentUser(Account account){
        this.account = account;
    }
}