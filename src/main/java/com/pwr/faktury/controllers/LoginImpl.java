package com.pwr.faktury.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.pwr.faktury.api.LoginApiDelegate;
import com.pwr.faktury.model.Login;
import com.pwr.faktury.model.UserDetails;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.jwt.JwtUtils;
import com.pwr.faktury.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginImpl implements LoginApiDelegate {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<UserDetails> login(Login login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Optional<User> user = userRepository.findByLogin(userDetails.getLogin());
        if (user.isPresent()) {
            UserDetails uDetails = new UserDetails().id(userDetails.getId()).login(userDetails.getLogin())
                    .accessToken(jwt).roles(roles).tokenType("Bearer").company(user.get().getPersonal_data());
            return new ResponseEntity<>(uDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    
}
