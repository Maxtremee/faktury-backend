package com.pwr.faktury.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Void> deleteProduct(String id) {
        User user = userService.getUser();
        if (user != null) {
            for (Product temp : user.getProducts()) {
                if (temp.getId().equals(id)) {
                    productRepository.deleteById(id);
                    user.getProducts().remove(temp);
                    userRepository.save(user);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }
            }
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<List<Product>> getProducts(String searchstr) {
        User user = userService.getUser();
        if (user != null) {
            //return all if searchstr empty
            if (searchstr == "" | searchstr.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<Product>(user.getProducts()), HttpStatus.OK);
            }

            //filter products with searchstr
            List<Product> foundProducts = new ArrayList<>();
            for (Product temp : user.getProducts()) {
                if (temp.getName().contains(searchstr)) {
                    foundProducts.add(temp);
                }
            }
            //if none found return 404
            if (foundProducts.size() > 0) {
                return new ResponseEntity<>(foundProducts, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> newProduct(Product product) {
        User user = userService.getUser();
        if (user != null) {
            if (!user.getProducts().isEmpty()) {
                for (Product temp : user.getProducts()) {
                    if (temp.getName().equals(product.getName())) {
                        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                    }
                }
            }
            productRepository.save(product);
            user.getProducts().add(product);
            userRepository.save(user);
            return new ResponseEntity<Void>(HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> updateProduct(String id, Product product) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Product> temp = productRepository.findById(id);
            if (temp.isPresent()) {
                product.setId(id);
                productRepository.save(product);
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
