package com.arsoft.projects.thevibgyor.shopping.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.arsoft.projects.thevibgyor.backend.model.PageItem;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.service.UserService;
import com.arsoft.projects.thevibgyor.shopping.DTO.ProductRequest;
import com.arsoft.projects.thevibgyor.shopping.DTO.ProductResponse;
import com.arsoft.projects.thevibgyor.shopping.entity.Product;
import com.arsoft.projects.thevibgyor.shopping.repository.ProductRepository;
import com.arsoft.projects.thevibgyor.shopping.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public String upsertProduct(ProductRequest productRequest, HttpServletRequest request) throws BadRequestException {
		String userName = String.valueOf(request.getAttribute("userName"));
		User user = userService.getUser(userName);
			validateInput(productRequest);
			Product product = null;
			String result = "";
			if (productRequest.getId() != null && !productRequest.getId().isEmpty()) {
				// product will be updated
				product = productRepository.findById(productRequest.getId())
						.orElseThrow(() -> new RuntimeException("Product not found by id :" + productRequest.getId()));
				product.setUpdatedAt(LocalDateTime.now());
				result = "updated";
			} else {
				product = new Product();
				product.setCreatedAt(LocalDateTime.now());
				product.setIsActive(Boolean.TRUE);
				product.setIsDeleted(Boolean.FALSE);
				result = "saved";
			}
			product.setActualPrice(productRequest.getActualPrice());
			if (productRequest.getDiscount() != null && productRequest.getDiscount() > 0)
				product.setOfferPrice(
						(productRequest.getActualPrice()) - (productRequest.getActualPrice() * productRequest.getDiscount() / 100));

			product.setOwner(user);
			product.setDiscription(productRequest.getDescription());
			product.setProductName(productRequest.getProductName());
			productRepository.save(product);
			return result;
	}

	@Override
	public PageItem<List<ProductResponse>> fetechProducts(int pageNo, int size) {
		Sort sort = Sort.by("createdAt").descending();
		PageRequest pageable = PageRequest.of(pageNo, size, sort);
		Page<Product> products = productRepository.findAll(pageable);
		List<ProductResponse> responseList = products.stream()
				.map(product -> new ProductResponse(product.getId(), product.getProductName(), product.getActualPrice(),
						product.getDiscription(), product.getOfferPrice(), product.getOwner()))
				.collect(Collectors.toList());
		return new PageItem<List<ProductResponse>>(pageNo, size, products.getTotalElements(), responseList);
	}
	
	@Override
	public void deleteProduct(String id) throws BadRequestException {
			if (id==null || id.isEmpty()) {
				throw new BadRequestException("Product id is null");
			}
			Product product = productRepository.findById(id).orElseThrow(()->new BadRequestException("Product not found by id : "+id));
			productRepository.delete(product);
	}

	@Override
	public Boolean changeStatus(String id) throws BadRequestException {
		Product product;
			if (id==null || id.isEmpty()) {
				throw new BadRequestException("Product id is null");
			}
			product = productRepository.findById(id).orElseThrow(()->new BadRequestException("Product not found by id : "+id));
			product.setIsActive(product.getIsActive().equals(Boolean.TRUE)?Boolean.FALSE:Boolean.TRUE);
			product = productRepository.save(product);
		return product.getIsActive();
	}
	private void validateInput(ProductRequest product) throws BadRequestException {
		if (product.getProductName() == null || product.getProductName().isEmpty()) {
			throw new BadRequestException("Product Name is null");
		}
		if (product.getDescription() == null || product.getDescription().isEmpty()) {
			throw new BadRequestException("Product description needs to be given");
		}
		if (product.getActualPrice() == null || product.getActualPrice() <= 0) {
			throw new BadRequestException("Product price should be valid");
		}
	}

}
