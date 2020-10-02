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
import com.tony.automationserverweb.model.Application;
import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.User;
import com.tony.automationserverweb.helper.Helper;
import com.tony.automationserverweb.mapper.AccountRowMapper;
import com.tony.automationserverweb.mapper.ApplicationRowMapper;
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

    @Autowired
    private ApplicationRowMapper applicationRowMapper;

    private static final String countQuery = "SELECT count(*) FROM account WHERE email = ?";
    private static final String insertQuery = "INSERT INTO account (email, password, token, nickname, otp, q1, q2, ans1, ans2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE account SET email = ?, password = ?, token = ?, nickname = ?, otp = ?, q1 = ?, q2 = ?, ans1 = ?, ans2 = ? WHERE id = ?";
    // private static final String selectQuery = "SELECT account.id account_id, nickname, email, password, token, otp, device.id device_id, device_key, device.connected d_connected, user.id user_id, user_key, user.connected u_connected FROM account LEFT JOIN device ON account.id = device.account_id LEFT JOIN user ON account.id = user.account_id LEFT JOIN subscriptions ON subscriptions.account_id = account.id LEFT JOIN application ON application.id = subscriptions.app_id ";
    private static final String selectQuery = "SELECT * FROM (SELECT 1 x, account.id account_id, nickname, email, password, token, otp, null device_id, null device_key, null d_connected, null user_id, null user_key, null u_connected, null app_id, null app_name, null app_token, null dev_account_id, q1, q2, ans1, ans2 FROM account "+
    "UNION ALL SELECT 2, subscriptions.account_id, null, null, null, null, null, null, null, null, null, null, null, application.id app_id, app_name, app_token, dev_account_id, null, null, null, null FROM application INNER JOIN subscriptions ON subscriptions.app_id = application.id INNER JOIN account ON account.id = subscriptions.account_id "+
    "UNION ALL SELECT 3, device.account_id, null, null, null, null, null, device.id device_id, device_key, device.connected d_connected, null, null, null, device.app_id, null, app_token, null, null, null, null, null FROM device LEFT JOIN application ON device.app_id = application.id "+
    "UNION ALL SELECT 4, user.account_id, null, null, null, null, null, null, null, null, user.id user_id, user_key, user.connected u_connected, user.app_id, null, app_token, null, null, null, null, null FROM user LEFT JOIN application ON user.app_id = application.id) a ";
    private static final String uniqueTokenQuery = "SELECT COUNT(*) FROM account WHERE token = ?";
    private static final String orderBySelect = " ORDER BY account_id, x";
    private static final String subscribeQuery = "INSERT INTO subscriptions (account_id, app_id) VALUES (?, ?)";
    private static final String unsubscribeQuery = "DELETE FROM subscriptions WHERE account_id =? AND app_id = ?";

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
            ps.setInt(6, object.getQ1());
            ps.setInt(7, object.getQ2());
            ps.setString(8, object.getAnswer1());
            ps.setString(9, object.getAnswer2());
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
            ps.setInt(6, object.getQ1());
            ps.setInt(7, object.getQ2());
            ps.setString(8, object.getAnswer1());
            ps.setString(9, object.getAnswer2());
            ps.setLong(10, object.getId());
            return ps;
        });
        return object;
    }

    @Override
    public Account findOneById(Long id) {
        List<Account> users = getAllByQuery(selectQuery.concat(" WHERE account_id = " + id + orderBySelect));
        return users.get(0);
    }

    @Override
    public List<Account> getAll() {
        return getAllByQuery(selectQuery.concat(orderBySelect));
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

    public String generateUniqueToken(){
        int count = 0;
        String token = null;
        do{
            token = Helper.generateToken();
            count = jdbcTemplate.queryForObject(uniqueTokenQuery, new Object[]{token}, Integer.class);
        }while(count != 0);
        return token;
    }

    public Account getUserByEmail(String email){
        List<Account> list = jdbcTemplate.query(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(selectQuery.concat(" WHERE email = ?").concat(orderBySelect));
                ps.setString(1, email);
                return ps;
            }
        }, new AccountListSetExtractor());

        if(list.size() == 0)
            return null;
        return list.get(0);
    }

    public Account getAccountByIdForApplication(long id, String appToken) {
        List<Account> list = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con
                        .prepareStatement(selectQuery.concat(" WHERE account_id = ? and (x = 1 or app_token = ?)").concat(orderBySelect));
                ps.setLong(1, id);
                ps.setString(2, appToken);
                return ps;
            }
        }, new AccountListSetExtractor());

        if (list.size() == 0)
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
            int appIdx = 0;
            
            while (rs.next()) {

                int x = rs.getInt("x");
                if(x == 1){
                    account = accountRowMapper.mapRow(rs, usrIdx++);
                    account.setDevices(new LinkedList<>());
                    deviceRowMapper.setParentUser(account);
                    userRowMapper.setParentUser(account);
                    users.add(account);
                }else if (x == 2){
                    Application d = applicationRowMapper.mapRow(rs, appIdx++);
                    account.getSubscriptions().add(d);
                }else if (x == 3){
                    Long rowAppId = rs.getLong("app_id");
                    Application app = Helper.getAppFromList(account.getSubscriptions(), rowAppId);
                    deviceRowMapper.setApplication(app);
                    Device d = deviceRowMapper.mapRow(rs, deviceIdx++);
                    account.getDevices().add(d);
                }else if (x == 4){
                    Long rowAppId = rs.getLong("app_id");
                    Application app = Helper.getAppFromList(account.getSubscriptions(), rowAppId);
                    userRowMapper.setApplication(app);
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

    public void subscribe(Account object, Application application) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(subscribeQuery);
            ps.setLong(1, object.getId());
            ps.setLong(2, application.getId());
            return ps;
        });
    }

    public Account unsubscribe(Account object, Application application) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(unsubscribeQuery);
            ps.setLong(1, object.getId());
            ps.setLong(2, application.getId());
            return ps;
        });
        return object;
    }
}