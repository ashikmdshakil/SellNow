package com.ecommerce.repository;

import org.springframework.stereotype.Repository;

import com.ecommerce.bean.User;
@Repository
public interface MyProfile {
	public User getUserDetails(String name);
	public User getUserProfile(int id);
}
