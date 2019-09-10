package com.example.Batch;

import com.example.model.Employee;

public class EmployeeItemProcessor implements org.springframework.batch.item.ItemProcessor<Employee, String>{

	@Override
	public String process(Employee employee) throws Exception {
		
		String s = "Hi iam " + employee.getName() + employee.getDesignation() + "from "+ employee.getCity();
		
		return s;
	}

}
