package com.example.demo;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.example.model.Person;
@Component
public class PersonFieldSetMapper implements FieldSetMapper<Person>{

	@Override
	public Person mapFieldSet(FieldSet fieldSet) throws BindException {
		
		Person person = new Person();
		person.setId(fieldSet.readInt(0));
		person.setFirstName(fieldSet.readString(1));
		person.setLastName(fieldSet.readString(2));
		return person;
	}

}
