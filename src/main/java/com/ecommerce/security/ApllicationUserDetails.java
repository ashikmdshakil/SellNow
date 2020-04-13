package com.ecommerce.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.bean.User;

public class ApllicationUserDetails implements UserDetails {

	
	private static final long serialVersionUID = -520038710270001185L;
	
	private String userName;
	private String password;
	private List<SimpleGrantedAuthority> authorities;
	

	public ApllicationUserDetails(User user) {
		super();
		this.userName = user.getEmailAdress();
		this.password = user.getPassword();
		this.authorities = Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	

	public ApllicationUserDetails() {
		super();
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
