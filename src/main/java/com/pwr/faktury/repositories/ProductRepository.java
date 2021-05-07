package com.pwr.faktury.repositories;

import java.util.Optional;

import com.pwr.faktury.model.Product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(@Param("name") String name);
}
