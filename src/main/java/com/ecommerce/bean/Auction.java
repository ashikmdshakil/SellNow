  package com.ecommerce.bean;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "auction")
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auction_id")
	private int id;
	private String title;
	private String productDescription;
	@Lob
	private byte[] productImages;
	private String base64image;
	private LocalDateTime startTime;
	private LocalDateTime deadTime;
	@OneToOne(cascade =CascadeType.REFRESH )
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	@OneToOne(mappedBy = "auction")
	private BidRate bidRate;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    
	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public byte[] getProductImages() {
		return productImages;
	}

	public void setProductImages(byte[] productImages) {
		this.productImages = productImages;
	}

	

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(LocalDateTime deadTime) {
		this.deadTime = deadTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getBase64image() {
		return base64image;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBase64image(String base64image) {
		this.base64image = base64image;
	}
	
	

}
