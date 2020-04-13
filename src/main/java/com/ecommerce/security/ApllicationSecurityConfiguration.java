package com.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class ApllicationSecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService userDetailsService;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/hello").hasAuthority("user")
		.antMatchers("/ecommerce/Auctions").hasAuthority("user")
		.antMatchers("/ecommerce").permitAll()
		.antMatchers("/ecommerce/signup").permitAll()
		.antMatchers("/ecommerce/resetPassword").permitAll()
		.antMatchers("/ecommerce/mailForReset").permitAll()
		.antMatchers("/ecommerce/resetPass/*").permitAll()
		.and().formLogin()
		.loginPage("/login")
		.defaultSuccessUrl("/getSession").permitAll()
		.failureUrl("/loginFailed").permitAll()
		.and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/ecommerce").permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/denied");
		
		
		

	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}
