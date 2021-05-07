package com.pwr.faktury.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pwr.faktury.api.SignupApiDelegate;
import com.pwr.faktury.models.ERole;
import com.pwr.faktury.models.Role;
import com.pwr.faktury.model.Signup;
import com.pwr.faktury.models.User;
import com.pwr.faktury.models.adapters.ContractorAdapter;
import com.pwr.faktury.repositories.RoleRepository;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupImpl implements SignupApiDelegate {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<Void> signup(Signup signup) {
        if (userRepository.existsByLogin(signup.getLogin())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        User user = new User();
        user.setLogin(signup.getLogin());
        user.setPassword(encoder.encode(signup.getPassword()));
        user.setPersonal_data((ContractorAdapter) signup.getCompany());

        List<String> strRoles = signup.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "MODERATOR":
                        Role modRole = roleRepository.findByName(ERole.MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        
        userRepository.save(user);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
