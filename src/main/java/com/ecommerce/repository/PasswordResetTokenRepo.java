package com.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.bean.Token;
@Repository
public interface PasswordResetTokenRepo extends JpaRepository<Token, Integer> {
	public Token findByUserId(int id);
	public void deleteByUserEmailAdress(String mail);
	@Query("from BidRate where user_id = :id")
	public void deleteAllByUserId(int id);
}
