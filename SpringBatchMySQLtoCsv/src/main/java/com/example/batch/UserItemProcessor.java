package com.example.batch;

import com.example.model.User;

public class UserItemProcessor implements org.springframework.batch.item.ItemProcessor<User, User>{

	@Override
	public User process(User user) throws Exception {
		
		
		User u = new User();
		u.setId(user.getId());
		u.setName(user.getName());
		
		return u;
	}

}
