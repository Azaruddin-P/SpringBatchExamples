package com.example.demo;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person>{

	@Override
	public Person process(Person person) throws Exception {
		
		System.out.println("processing...."+person);
		System.out.println(person.getId());
		System.out.println(person.getFirstName());
		System.out.println(person.getLastName());

		
		return person ;
	}
	
	

}
