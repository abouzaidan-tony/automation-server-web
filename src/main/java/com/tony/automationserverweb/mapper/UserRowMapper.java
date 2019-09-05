package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.dao.AccountRepositoryImpl;
import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Autowired
    private AccountRepositoryImpl accountRepository;

    private Account account;

    @Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User d = new User();
        d.setId(rs.getLong("user_id"));
        d.setKey(rs.getString("user_key"));
        d.setConnected(rs.getBoolean("u_connected"));
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