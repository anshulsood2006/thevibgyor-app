package com.arsoft.projects.thevibgyor.shopping.controller;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import com.arsoft.projects.thevibgyor.backend.model.GenericResponse;
import com.arsoft.projects.thevibgyor.backend.model.GenericResponseInfo;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.backend.model.PageItem;
import com.arsoft.projects.thevibgyor.common.constant.ApiUri;
import com.arsoft.projects.thevibgyor.common.constant.HttpStatus;
import com.arsoft.projects.thevibgyor.common.exception.GenericException;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import com.arsoft.projects.thevibgyor.shopping.DTO.ProductRequest;
import com.arsoft.projects.thevibgyor.shopping.DTO.ProductResponse;
import com.arsoft.projects.thevibgyor.shopping.constant.ProductApiUri;
import com.arsoft.projects.thevibgyor.shopping.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(ApiUri.SUPER_ADMIN + ProductApiUri.PRODUCT)
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping(value = ProductApiUri.SAVE)
	public GenericResponse<?> addProduct(@RequestAttribute("requestTime") ZonedDateTime requestTime,
			HttpServletRequest request, @RequestBody ProductRequest product) throws BadRequestException {
		log.info("Request has reached product controller now");
		Header header;
		ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
		String url = HttpRequestUtil.getFullURL(request);
		try {
			String result = productService.upsertProduct(product, request);
			GenericResponseInfo<String> genericResponseInfo = new GenericResponseInfo<>(false, "product", 1,
					"Product " + result + " successfully.");
			long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
			header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
			return new GenericResponse<String>(header, genericResponseInfo);
		} catch (Exception e) {
			log.error(String.format("Exception occurred %s", e.getMessage()));
			throw new GenericException(e, requestTime);
		}
	}

	@PutMapping(value = ProductApiUri.UPDATE)
	public GenericResponse<?> updateProduct(@RequestAttribute("requestTime") ZonedDateTime requestTime,
			HttpServletRequest request, @RequestBody ProductRequest product) throws BadRequestException {
		log.info("Request has reached product controller now");
		Header header;
		ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
		String url = HttpRequestUtil.getFullURL(request);
		try {
			String result = productService.upsertProduct(product, request);
			GenericResponseInfo<String> genericResponseInfo = new GenericResponseInfo<>(false, "product", 1,
					"Product " + result + " successfully.");
			long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
			header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
			return new GenericResponse<String>(header, genericResponseInfo);
		} catch (Exception e) {
			log.error(String.format("Exception occurred %s", e.getMessage()));
			throw new GenericException(e, requestTime);
		}
	}

	@GetMapping(value = ProductApiUri.PRODUCTS)
	public GenericResponse<?> fetchProducts(@RequestAttribute("requestTime") ZonedDateTime requestTime,
			HttpServletRequest request, @RequestParam int pageNo, @RequestParam int size) throws BadRequestException {
		log.info("Request has reached product controller now");
		Header header;
		ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
		String url = HttpRequestUtil.getFullURL(request);
		try {
			PageItem<List<ProductResponse>> fetechedProducts = productService.fetechProducts(pageNo, size);
			GenericResponseInfo<PageItem<List<ProductResponse>>> genericResponseInfo = new GenericResponseInfo<>(
					Boolean.TRUE, "products", (int) fetechedProducts.getTotalElements(), fetechedProducts);
			long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
			header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
			return new GenericResponse<>(header, genericResponseInfo);
		} catch (Exception e) {
			log.error(String.format("Exception occurred %s", e.getMessage()));
			throw new GenericException(e, requestTime);
		}
	}

	@DeleteExchange(value = ProductApiUri.DELETE)
	public GenericResponse<?> deleteProduct(@RequestAttribute("requestTime") ZonedDateTime requestTime,
			HttpServletRequest request, @RequestParam String id) throws BadRequestException {
		log.info("Request has reached product controller now");
		Header header;
		ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
		String url = HttpRequestUtil.getFullURL(request);
		try {
			productService.deleteProduct(id);
			GenericResponseInfo<String> genericResponseInfo = new GenericResponseInfo<>(Boolean.FALSE, "product", 1,
					"Product deleted successfully.");
			long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
			header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
			return new GenericResponse<>(header, genericResponseInfo);
		} catch (Exception e) {
			log.error(String.format("Exception occurred %s", e.getMessage()));
			throw new GenericException(e, requestTime);
		}
	}

	@PatchMapping(value = ProductApiUri.ACTIVATE)
	public GenericResponse<?> changeStatus(@RequestAttribute("requestTime") ZonedDateTime requestTime,
			HttpServletRequest request, @RequestParam String id) throws BadRequestException {
		log.info("Request has reached product controller now");
		Header header;
		ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
		String url = HttpRequestUtil.getFullURL(request);
		try {
			Boolean status = productService.changeStatus(id);
			String msg = status ? "Product activated successfully." : "Product deactivated successfully.";
			GenericResponseInfo<String> genericResponseInfo = new GenericResponseInfo<>(status, "product", 1, msg);
			long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
			header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
			return new GenericResponse<>(header, genericResponseInfo);
		} catch (Exception e) {
			log.error(String.format("Exception occurred %s", e.getMessage()));
			throw new GenericException(e, requestTime);
		}
	}
	
	@GetMapping(value = ProductApiUri.FIND_BY_ID)
	public GenericResponse<?> findById(@RequestAttribute("requestTime") ZonedDateTime requestTime,
			HttpServletRequest request, @PathVariable String id) throws BadRequestException {
		log.info("Request has reached product controller now");
		Header header;
		ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
		String url = HttpRequestUtil.getFullURL(request);
		try {
			GenericResponseInfo<ProductResponse> genericResponseInfo = new GenericResponseInfo<>(Boolean.FALSE, "product", 1, productService.fetchProduct(id));
			long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
			header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
			return new GenericResponse<>(header, genericResponseInfo);
		} catch (Exception e) {
			log.error(String.format("Exception occurred %s", e.getMessage()));
			throw new GenericException(e, requestTime);
		}
	}

}
