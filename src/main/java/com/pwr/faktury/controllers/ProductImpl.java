package com.pwr.faktury.controllers;

import java.util.Optional;

import com.pwr.faktury.api.ProductApiDelegate;
import com.pwr.faktury.model.Product;
import com.pwr.faktury.models.User;
import com.pwr.faktury.models.adapters.ProductAdapter;
import com.pwr.faktury.repositories.ProductRepository;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductImpl implements ProductApiDelegate {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Void> newProduct(Product product) {
        ProductAdapter productAdapter = new ProductAdapter(product);
        Authentication authentication = 
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        if (user.isPresent()) {
            Optional<Product> existing_product = productRepository.findByName(product.getName());
            if (existing_product.isPresent()) {
                return new ResponseEntity<Void>(HttpStatus.CONFLICT);
            } else {
                productRepository.save(productAdapter.get());
                user.get().getProducts().add(productAdapter.get());
                return new ResponseEntity<Void>(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
