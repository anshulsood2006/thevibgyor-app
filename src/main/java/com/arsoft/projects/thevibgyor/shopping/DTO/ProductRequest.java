package com.arsoft.projects.thevibgyor.shopping.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
	
	private String id;
	
	private String productName;
	
	private Double actualPrice;
	
	private Double discount;
	
	private String description;

}
