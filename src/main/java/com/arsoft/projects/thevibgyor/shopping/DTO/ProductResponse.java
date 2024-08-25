package com.arsoft.projects.thevibgyor.shopping.DTO;

import com.arsoft.projects.thevibgyor.backend.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

	private String id;

	private String productName;

	private Double actualPrice;

	private String description;

	private Double offerPrice;
	
	private Boolean isActive;

	private User owner;

//	public ProductResponse(Product product) {
//		this.id = product.getId();
//		this.productName = product.getProductName();
//		this.actualPrice = product.getActualPrice();
//		this.description = product.getDiscription();
//		this.offerPrice = product.getOfferPrice();
//		this.owner = product.getOwner();
//	}

}
