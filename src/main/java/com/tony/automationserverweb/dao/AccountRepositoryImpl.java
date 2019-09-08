package com.tony.automationserverweb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tony.automationserverweb.model.Account;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.User;
import com.tony.automationserverweb.mapper.AccountRowMapper;
import com.tony.automationserverweb.mapper.DeviceRowMapper;
import com.tony.automationserverweb.mapper.UserRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AccountRepositoryImpl implements IRepository<Account, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private UserRowMapper userRowMapper;

    @Autowired
    private AccountRowMapper accountRowMapper;

    @Autowired 
    private DeviceRowMapper deviceRowMapper;

    private static final String countQuery = "SELECT count(*) FROM account WHERE email = ?";
    private static final String insertQuery = "INSERT INTO account (email, password, token, nickname, otp) VALUES (?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE account SET email = ?, password = ?, token = ?, nickname = ?, otp = ? WHERE id = ?";
    private static final String selectQuery = "SELECT account.id account_id, nickname, email, password, token, otp, device.id device_id, device_key, device.connected d_connected, user.id user_id, user_key, user.connected u_connected FROM account LEFT JOIN device ON account.id = device.account_id LEFT JOIN user ON account.id = user.account_id ";
    private static final String uniqueTokenQuery = "SELECT COUNT(*) FROM account WHERE token = ?";

    @Override
    @Transactional
    public Account insert(Account object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, object.getEmail());
            ps.setString(2, object.getPasswordHash());
            ps.setString(3, object.getToken());
            ps.setString(4, object.getNickname());
            ps.setString(5, object.getOtp());
            return ps;
        }, keyHolder);

        object.setId(keyHolder.getKey().longValue());

        for (Device d : object.getDevices()) {
            d.setAccount(object);
            deviceRepositoryImpl.insert(d);
        }
        for (User d : object.getUsers()) {
            d.setAccount(object);
            userRepositoryImpl.insert(d);
        }
        return object;
    }

    @Override
    public Account update(Account object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, object.getEmail());
            ps.setString(2, object.getPasswordHash());
            ps.setString(3, object.getToken());
            ps.setString(4, object.getNickname());
            ps.setString(5, object.getOtp());
            ps.setLong(6, object.getId());
            return ps;
        });
        return object;
    }

    @Override
    public Account findOneById(Long id) {
        List<Account> users = getAllByQuery(selectQuery.concat(" WHERE account.id = " + id));
        return users.get(0);
    }

    @Override
    public List<Account> getAll() {
        return getAllByQuery(selectQuery);
    }

    private List<Account> getAllByQuery(String query){
        return jdbcTemplate.query(query, new AccountListSetExtractor());
    }
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDeviceRowMapper(UserRowMapper userRowMapper) {
        this.userRowMapper = userRowMapper;
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String generateToken(){
        StringBuilder builder = new StringBuilder();
        int count = 30;
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public String generateUniqueToken(){
        int count = 0;
        String token = null;
        do{
            token = generateToken();
            count = jdbcTemplate.queryForObject(uniqueTokenQuery, new Object[]{token}, Integer.class);
        }while(count != 0);
        return token;
    }

    public Account getUserByEmail(String email){
        List<Account> list = jdbcTemplate.query(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(selectQuery.concat(" WHERE email = ?"));
                ps.setString(1, email);
                return ps;
            }
        }, new AccountListSetExtractor());

        if(list.size() == 0)
            return null;
        return list.get(0);
    }


    class AccountListSetExtractor implements ResultSetExtractor<List<Account>> {

        @Override
        public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Account> users = new ArrayList<Account>();
            Account account = null;
            int usrIdx = 0;
            int deviceIdx = 0;
            int userIdx = 0;
            Long deviceId = null;
            Long userId = null;
            while (rs.next()) {
                if (account == null || account.getId() != rs.getLong("account_id")) {
                    account = accountRowMapper.mapRow(rs, usrIdx++);
                    account.setDevices(new LinkedList<>());
                    deviceRowMapper.setParentUser(account);
                    userRowMapper.setParentUser(account);
                    users.add(account);
                }

                Long a = rs.getLong("device_id");
                if (!rs.wasNull() && deviceId != a)
                {
                    deviceId = a;
                    Device d = deviceRowMapper.mapRow(rs, deviceIdx++);
                    account.getDevices().add(d);
                }
                

                Long b = rs.getLong("user_id");
                if (!rs.wasNull() && userId != b)
                {
                    userId = b;
                    User u = userRowMapper.mapRow(rs, userIdx++);
                    account.getUsers().add(u);
                }
                
            }
            return users;
        }
        
    }

    public Integer getCountUsersByEmail(String email){
        return jdbcTemplate.queryForObject(countQuery, new Object[] {email}, Integer.class);
    }
}