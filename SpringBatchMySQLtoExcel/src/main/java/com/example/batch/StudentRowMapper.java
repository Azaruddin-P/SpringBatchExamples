package com.example.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.model.Student;

public class StudentRowMapper implements RowMapper<Student>{

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student s = new Student();
		s.setFirstName(rs.getString("firstName"));
		s.setLastName(rs.getString("lastName"));
		s.setEmail(rs.getNString("email"));
		return s;
	}

	
}