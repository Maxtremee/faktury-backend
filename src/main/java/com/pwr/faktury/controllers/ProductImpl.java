package com.pwr.faktury.controllers;

import com.pwr.faktury.api.ProductApiDelegate;
import com.pwr.faktury.model.Product;
import com.pwr.faktury.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductImpl implements ProductApiDelegate {
    private UserRepository userRepository;

    @Autowired
    public ProductImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Void> newProduct(Product product) {
        if (product != null) {
            if (userRepository.productExistsByName(product.getName())) {
                return new ResponseEntity<Void>(HttpStatus.CONFLICT);
            }
            //User user = userRepository.findById(id);
            
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
}
