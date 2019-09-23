package com.example.batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Student;

public class StudentItemProcessor implements ItemProcessor<Student, Student> {

	@Override
	public Student process(Student item) throws Exception {
		Student s =new Student();
		s.setFirstName(item.getFirstName());
		s.setLastName(item.getLastName());
		s.setEmail(item.getEmail());
		return s;
	}

}
