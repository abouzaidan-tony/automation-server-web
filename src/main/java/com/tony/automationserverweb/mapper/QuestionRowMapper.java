package com.tony.automationserverweb.mapper;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tony.automationserverweb.model.Question;

import org.springframework.jdbc.core.RowMapper;

@Component
public class QuestionRowMapper implements RowMapper<Question> {

    @Override
	public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
        Question u = new Question();
        u.setId(rs.getInt("id"));
        u.setQuestion(rs.getString("question"));
		return u;
	}

}