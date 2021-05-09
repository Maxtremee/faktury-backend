package com.pwr.faktury.controllers;

import com.pwr.faktury.api.ContractorApiDelegate;
import com.pwr.faktury.model.Contractor;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.ContractorRepository;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ContractorImpl implements ContractorApiDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Void> newContractor(Contractor contractor) {
        User user = userService.getUser();
        if (user != null) {
            if (!user.getContractors().isEmpty()) {
                for (Contractor temp : user.getContractors()) {
                    if (temp.getNip().equals(contractor.getNip())) {
                        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
                    }
                }
            }
            contractorRepository.save(contractor);
            user.getContractors().add(contractor);
            userRepository.save(user);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
