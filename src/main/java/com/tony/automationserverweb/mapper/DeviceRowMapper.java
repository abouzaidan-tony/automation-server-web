package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.dao.ApplicationRepositoryImpl;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.Device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

@Component
public class DeviceRowMapper implements RowMapper<Device> {

    @Autowired
    private AccountRepositoryImpl accountRepository;

    @Autowired
    private ApplicationRepositoryImpl applicationRepositoryImpl;

    private Account account;

    private Application application;

    @Override
    public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
        Device d = new Device();
        d.setId(rs.getLong("device_id"));
        d.setKey(rs.getString("device_key"));
        d.setConnected(rs.getBoolean("d_connected"));
        Long id = rs.getLong("account_id");
        if (account != null)
            d.setAccount(account);
        else if (!rs.wasNull())
            d.setAccount(accountRepository.findOneById(id));
        Long appId = rs.getLong("app_id");
        if (application != null)
            d.setApplication(application);
        else if (!rs.wasNull())
            d.setApplication(applicationRepositoryImpl.findOneById(appId));
        return d;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setParentUser(Account account) {
        this.account = account;
    }
}