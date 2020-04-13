package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.bean.Auction;
import com.ecommerce.bean.BidRate;
@Repository
public interface BidRateRepo extends JpaRepository<BidRate, Integer> {
	 public List<BidRate> findByAuctionIdOrderByBidRateDesc(int id);
	 public List<BidRate> findByUserId(int id);
	 @Query("from BidRate where user_id = :uid")
	 public void deleteAllByUserId(int uid);
	 //public void deleteByAuctionId(int id);
	// @Query("select distinct auction_id , title, product_images,dead_time from bid_rate inner join auction on bid_rate.auction_id = auction.id where bid_rate.user_id = :id")	
	 
}
