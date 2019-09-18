package com.example.batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.User;

public class UserItemProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User user) throws Exception {
		
		User u = new User();
		u.setName(user.getName());
		return u;
	}

}
