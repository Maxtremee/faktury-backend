package com.pwr.faktury.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import com.pwr.faktury.api.ProductApiDelegate;
import com.pwr.faktury.model.Product;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.ProductRepository;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class ProductImpl implements ProductApiDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Void> deleteProduct(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Product> product_to_check = user.getProducts().stream()
                    .filter(p -> p.getId().equals(id)).findAny();
            if (product_to_check.isPresent()) {
                productRepository.deleteById(id);
                user.getProducts().remove(product_to_check.get());
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Product> getProductWithId(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Product> product_to_get = user.getProducts().stream().filter(p -> p.getId().equals(id))
                    .findAny();
            if (product_to_get.isPresent()) {
                return new ResponseEntity<>(product_to_get.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(String searchstr) {
        User user = userService.getUser();
        if (user != null) {
            //return all if searchstr empty
            if (searchstr == " " | searchstr.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<Product>(user.getProducts()), HttpStatus.OK);
            }
            //filter products with searchstr
            List<Product> foundProducts = user.getProducts().stream().filter(p -> p.getName().contains(searchstr))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(foundProducts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> newProduct(Product product) {
        User user = userService.getUser();
        if (user != null) {
            Set<Product> products = user.getProducts();
            if (!products.isEmpty()) {
                Optional<Product> product_to_check = user.getProducts().stream()
                        .filter(p -> p.getName().equals(product.getName())).findAny();
                if (product_to_check.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            productRepository.save(product);
            products.add(product);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> updateProduct(String id, Product product) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Product> product_to_update = user.getProducts().stream().filter(p -> p.getId().equals(id))
                    .findAny();
            if (product_to_update.isPresent()){
                product.setId(id);
                productRepository.save(product);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
