package com.ecommerce.bean;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;
@Component
@Entity
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne(cascade =CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	private String tokenString;
	private LocalDateTime expiredTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getTokenString() {
		return tokenString;
	}
	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}
	public LocalDateTime getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(LocalDateTime expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	
	
}
