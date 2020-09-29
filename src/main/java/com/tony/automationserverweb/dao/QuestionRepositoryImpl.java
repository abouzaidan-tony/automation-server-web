package com.tony.automationserverweb.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.tony.automationserverweb.model.Device;
import com.tony.automationserverweb.model.Question;
import com.tony.automationserverweb.mapper.QuestionRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepositoryImpl implements IRepository<Question, Long> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired 
    private QuestionRowMapper questionRowMapper;

    private static final String deleteQuery = "DELETE FROM questions WHERE id = ?";
    private static final String insertQuery = "INSERT INTO questions (question) VALUES (?)";
    private static final String updateQuery = "UPDATE questions SET question = ? WHERE id = ?";
    private static final String selectQuery = "SELECT id, question FROM questions";

    @Override
    public Question insert(Question object) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, object.getQuestion());
            return ps;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        return object;
    }

    @Override
    public Question update(Question object) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, object.getQuestion());
            ps.setLong(2, object.getId());

            return ps;
        });
        return object;
    }

    @Override
    public Question findOneById(Long id) {
        return jdbcTemplate.queryForObject(selectQuery.concat(" WHERE id = ?"), new Object[]{id}, questionRowMapper);
    }

    @Override
    public List<Question> getAll() {
        return jdbcTemplate.query(selectQuery, questionRowMapper);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDeviceRowMapper(QuestionRowMapper questionRowMapper){
        this.questionRowMapper = questionRowMapper;
    }

    public void delete(Device device){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setLong(1, device.getId());
            return ps;
        });
    }
}