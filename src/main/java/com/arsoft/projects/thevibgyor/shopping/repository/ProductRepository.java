package com.arsoft.projects.thevibgyor.shopping.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arsoft.projects.thevibgyor.shopping.entity.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
