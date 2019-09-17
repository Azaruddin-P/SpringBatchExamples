package com.example.demo;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person>{

	@Override
	public Person process(Person person) throws Exception {
		
		Person p = new Person();
		p.setId(person.getId());
		p.setFirstName(person.getFirstName());
		p.setLastName(person.getLastName());
		return p ;
	}
	
	

}
