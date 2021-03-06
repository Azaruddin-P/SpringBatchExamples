package com.example.Batch;

import com.example.model.Employee;

public class EmployeeItemProcessor implements org.springframework.batch.item.ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee employee) throws Exception {
		
		
		Employee e = new Employee();
		e.setName(employee.getName());
		e.setDesignation(employee.getDesignation());
		e.setCity(employee.getCity());
		
		return e;
	}

}
