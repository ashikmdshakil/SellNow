package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.bean.Token;
import com.ecommerce.bean.User;
import com.ecommerce.repository.PasswordResetTokenRepo;
import com.ecommerce.repository.UserRepo;

@Service
public class PasswordReset {
	@Autowired
	private Token token;
	@Autowired
	private PasswordResetTokenRepo passReset;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private User user;

	public boolean getUser(String mail) {
		boolean result = false;
		if (userRepo.existsByEmailAdress(mail)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public boolean previousTokenExpred(String mail) {

		boolean result = false;
		LocalDateTime time = LocalDateTime.now();
		token = passReset.findByUserId(userRepo.findByEmailAdress(mail).getId());
		if (token == null) {
			result = true;
		} else if (token.getExpiredTime().isBefore(time)) {
			passReset.delete(token);
			result = true;
		} else if (token.getExpiredTime().isAfter(time)) {
			result = false;
		}

		return result;
	}

	public Token previousToken(String mail) {
		token = passReset.findByUserId(userRepo.findByEmailAdress(mail).getId());
		return token;
	}

	public Token sendNewToken(String mail) {

		LocalDateTime time = LocalDateTime.now();
		Token tokenNew = new Token();
		user = userRepo.findByEmailAdress(mail);
		String tokenString = "http://localhost:8080/ecommerce/resetPass/" + UUID.randomUUID().toString() + "/"
				+ user.getId();
		tokenNew.setUser(user);
		tokenNew.setTokenString(tokenString);
		tokenNew.setExpiredTime(time.plusHours(5));
		passReset.save(tokenNew);

		return token;
	}

	public boolean isTokenTrue(int userId, String tokenString) {
		boolean result = false;
		//Token tokenN = new Token();
		token = passReset.findByUserId(userId);
		String url = "http://localhost:8080/ecommerce/resetPass/"+tokenString+"/"+userId;
		if(token ==  null) {
			result = false;
		}
		else if(token.getTokenString().equals(url) && token.getExpiredTime().isAfter(LocalDateTime.now())) {
			result = true;
		}
		else if(!(token.getTokenString().equals(tokenString)) || token.getExpiredTime().isBefore(LocalDateTime.now())) {
			result = false;
		}
		
		return result;
	}

}
