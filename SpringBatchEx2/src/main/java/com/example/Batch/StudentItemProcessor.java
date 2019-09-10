package com.example.Batch;



import com.example.model.Student;

public class StudentItemProcessor implements org.springframework.batch.item.ItemProcessor<Student, String>{

	@Override
	public String process(Student student) throws Exception {
		
		String greeting = "Hello " + " "+student.getName()+ " "+student.getCourse()+ " "+student.getCity();
		
		return greeting;
	}

}
