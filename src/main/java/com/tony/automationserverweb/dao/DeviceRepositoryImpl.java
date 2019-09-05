package com.tony.automationserverweb.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.mapper.DeviceRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceRepositoryImpl implements IRepository<Device, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired 
    private DeviceRowMapper deviceRowMapper;

    private static final String deleteQuery = "DELETE FROM device WHERE id = ?";
    private static final String insertQuery = "INSERT INTO device (device_key, account_id, connected) VALUES (?, ?, 0)";
    private static final String updateQuery = "UPDATE device SET device_key = ?, account_id = ? WHERE id = ?";
    private static final String selectQuery = "SELECT id device_id, device_key, connected d_connected, account_id FROM device";

    @Override
    public Device insert(Device object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, object.getKey());
            ps.setLong(2, object.getAccountId());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        return object;
    }

    @Override
    public Device update(Device object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, object.getKey());
            ps.setLong(2, object.getAccountId());
            return ps;
        });
        return object;
    }

    @Override
    public Device findOneById(Long id) {
        return jdbcTemplate.queryForObject(selectQuery.concat(" WHERE id = ?"), new Object[]{id}, deviceRowMapper);
    }

    @Override
    public List<Device> getAll() {
        return jdbcTemplate.query(selectQuery, deviceRowMapper);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDeviceRowMapper(DeviceRowMapper deviceRowMapper){
        this.deviceRowMapper = deviceRowMapper;
    }

    public void delete(Device device){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setLong(1, device.getId());
            return ps;
        });
    }
}