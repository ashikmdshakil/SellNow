package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.bean.Auction;
@Repository
public interface AuctionRepo extends JpaRepository<Auction, Integer> {
	public Auction findById(int id);
	public List<Auction> findByUserId(int id);
	 @Query("from Auction where id = :id")
	 public Auction findByUser(int id);
	 @Query("from Auction where user_id = :uid")
	 public void deleteByUserId(int uid);
	
}
