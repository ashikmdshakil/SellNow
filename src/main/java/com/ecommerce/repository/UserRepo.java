package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.bean.User;
@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
	public User findByEmailAdress(String mail);
	public boolean existsByEmailAdress(String mail);
	public User findById(int id);
}
