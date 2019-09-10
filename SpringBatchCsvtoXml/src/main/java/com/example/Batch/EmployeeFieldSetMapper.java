package com.example.Batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.example.model.Employee;

public class EmployeeFieldSetMapper implements FieldSetMapper<Employee> {

	@Override
	public Employee mapFieldSet(FieldSet fieldSet) throws BindException {
		
		Employee employee = new Employee();
		employee.setName(fieldSet.readString(0));
		employee.setDesignation(fieldSet.readString(1));
		employee.setCity(fieldSet.readString(2));
		
		return employee;
	}
	
	

}
