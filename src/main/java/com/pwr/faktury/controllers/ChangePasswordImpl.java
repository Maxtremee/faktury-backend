package com.pwr.faktury.controllers;

import com.pwr.faktury.api.ChangePasswordApiDelegate;
import com.pwr.faktury.model.NewPassword;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordImpl implements ChangePasswordApiDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;
    
    @Override
    public ResponseEntity<Void> changePassword(NewPassword newPassword) {
        User user = userService.getUser();
        if (user != null) {
            if (encoder.matches(newPassword.getOldPassword(), user.getPassword())) {
                user.setPassword(encoder.encode(newPassword.getNewPassword()));
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
