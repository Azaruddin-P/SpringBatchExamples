package com.example.Batch;


import org.springframework.batch.item.ItemProcessor;

import com.example.model.Employee;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee employee) throws Exception {
		
		
		Employee e = new Employee();
		e.setName(employee.getName());
		e.setDesignation(employee.getDesignation());
		e.setCity(employee.getCity());
		
		return e;
	}

}

