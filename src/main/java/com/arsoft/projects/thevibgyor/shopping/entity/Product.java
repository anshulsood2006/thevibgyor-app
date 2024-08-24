package com.arsoft.projects.thevibgyor.shopping.entity;

import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.ACTUAL_PRICE;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.DESCRIPTION;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.DOCUMENT_NAME;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.OFFER_PRICE;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.PRODUCT_NAME;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.PRODUCT_OWNER;

import java.io.Serializable;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.arsoft.projects.thevibgyor.backend.model.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection = DOCUMENT_NAME)
public class Product extends AbstractPersistable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Field(PRODUCT_NAME)
	private String productName;
	
	@Field(DESCRIPTION)
	private String discription;
	
	@Field(ACTUAL_PRICE)
	private Double actualPrice;
	
	@Field(OFFER_PRICE)
	private Double offerPrice;
	
	@Field(PRODUCT_OWNER)
	//@CreatedBy
	private User owner;
	
}
