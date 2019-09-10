package com.example.batch;



import com.example.model.Person;

public class PersonItemProcessor implements org.springframework.batch.item.ItemProcessor<Person, String>{

	@Override
	public String process(Person person) throws Exception {
		
		String greeting = "Hello " + person.getFirstName().toUpperCase() + " "
		        + person.getLastName().toUpperCase() + "!";

		return greeting;
	}

}
