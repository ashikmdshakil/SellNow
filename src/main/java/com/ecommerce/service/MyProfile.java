package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.bean.User;
import com.ecommerce.repository.UserRepo;
@Service
public class MyProfile implements com.ecommerce.repository.MyProfile {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private User user;
	
	@Override
	public User getUserDetails(String name) {
		user = userRepo.findByEmailAdress(name);
		return user;
		// TODO Auto-generated method stub

	}

	@Override
	public User getUserProfile(int id) {
		user = userRepo.findById(id);
		return user;
	}

}
