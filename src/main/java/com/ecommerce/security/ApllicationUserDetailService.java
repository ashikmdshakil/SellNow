package com.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.bean.User;
import com.ecommerce.repository.UserRepo;
@Service
public class ApllicationUserDetailService implements UserDetailsService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private User user;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		user = userRepo.findByEmailAdress(username);
		return new ApllicationUserDetails(user);
	}
	

}
