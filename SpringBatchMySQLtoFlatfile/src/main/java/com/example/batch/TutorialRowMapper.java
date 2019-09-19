package com.example.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.model.Tutorial;

public class TutorialRowMapper implements RowMapper<Tutorial> {

	@Override
	public Tutorial mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Tutorial t =new Tutorial();
		t.setId(rs.getInt("id"));
		t.setAuthor(rs.getNString("author"));
		t.setTitle(rs.getString("title"));
		return t;
	}

}
