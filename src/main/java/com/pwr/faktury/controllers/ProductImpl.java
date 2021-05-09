package com.pwr.faktury.controllers;

import java.util.Optional;

import com.pwr.faktury.api.ProductApiDelegate;
import com.pwr.faktury.model.Product;
import com.pwr.faktury.models.User;
import com.pwr.faktury.models.adapters.ProductAdapter;
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
    public ResponseEntity<Void> newProduct(Product product) {
        ProductAdapter productAdapter = new ProductAdapter(product);
        User user = userService.getUser();
        if (user != null) {
            if (!user.getProducts().isEmpty()) {
                for (Product temp : user.getProducts()) {
                    if (temp.getName().equals(productAdapter.get().getName())) {
                        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                    }
                }
            }
            productRepository.save(productAdapter.get());
            user.getProducts().add(productAdapter.get());
            userRepository.save(user);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
            
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

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
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
    }
}
