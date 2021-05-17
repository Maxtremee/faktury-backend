package com.pwr.faktury.controllers;

import com.pwr.faktury.api.ChangePersonalInfoApiDelegate;
import com.pwr.faktury.model.Contractor;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChangePersonalInfoImpl implements ChangePersonalInfoApiDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public ResponseEntity<Void> changePersonalInfo(Contractor contractor) {
        User user = userService.getUser();
        if (user != null) {
            user.setPersonal_data(contractor);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
