package com.example.batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Student;

public class StudentItemProcessor implements ItemProcessor<Student, Student>{

	public Student process(Student student) throws Exception {
		
		
		return student;
	}

}
