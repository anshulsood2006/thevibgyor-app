package com.arsoft.projects.thevibgyor.shopping.service;

import java.util.List;

import org.apache.coyote.BadRequestException;

import com.arsoft.projects.thevibgyor.backend.model.PageItem;
import com.arsoft.projects.thevibgyor.shopping.DTO.ProductRequest;
import com.arsoft.projects.thevibgyor.shopping.DTO.ProductResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface ProductService {

	String upsertProduct(ProductRequest productRequest, HttpServletRequest request) throws BadRequestException ;

	PageItem<List<ProductResponse>> fetechProducts(int pageNo, int size) throws BadRequestException;
	
	void deleteProduct(String id) throws BadRequestException;
	
	Boolean changeStatus(String id) throws BadRequestException;
}
