package com.ecommerce.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ManyToAny;
import org.springframework.stereotype.Component;

@Component
@Entity
public class BidRate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	private Long bidRate;
	@OneToOne(cascade = CascadeType.REFRESH )
	@JoinColumn(name = "auction_id", referencedColumnName = "auction_id")
	private Auction auction;
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

	public Long getBidRate() {
		return bidRate;
	}

	public void setBidRate(Long bidRate) {
		this.bidRate = bidRate;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}
	

}
