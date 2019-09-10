package com.example.Batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Employee;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee employee) throws Exception {
		
		String s = "Hi iam " + employee.getName() + employee.getDesignation() + "from "+ employee.getCity();
		
		return employee;
	}

}
