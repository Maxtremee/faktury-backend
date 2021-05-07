package com.pwr.faktury.security.services;

import java.util.Optional;

import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Optional<User> user = userRepository.findById(userDetails.getId());
            if (user.isPresent())
                return user.get();
            else
                return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
