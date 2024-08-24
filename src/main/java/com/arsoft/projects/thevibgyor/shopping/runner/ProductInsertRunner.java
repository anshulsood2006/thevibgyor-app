package com.arsoft.projects.thevibgyor.shopping.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.arsoft.projects.thevibgyor.shopping.DTO.ProductRequest;
import com.arsoft.projects.thevibgyor.shopping.service.ProductService;

@Component
public class ProductInsertRunner implements CommandLineRunner{

	@Autowired
	private ProductService productService;
	
	@Override
	public void run(String... args) throws Exception {
		ProductRequest product1 = new ProductRequest();
		product1.setActualPrice(2050.0);
		product1.setProductName("Smart Watch");
		product1.setDescription("It is a smart watch.");
		product1.setDiscount(0.0);
		//String result = productService.upsertProduct(product1);
		//System.out.println("Product "+result+" successfully.");
	}

	
}
