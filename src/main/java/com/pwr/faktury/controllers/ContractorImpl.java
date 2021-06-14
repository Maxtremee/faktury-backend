package com.pwr.faktury.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.pwr.faktury.api.ContractorApiDelegate;
import com.pwr.faktury.model.Contractor;
import com.pwr.faktury.model.InlineResponse201;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.ContractorRepository;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContractorImpl implements ContractorApiDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Void> deleteContractor(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Contractor> contractor_to_check = user.getContractors().stream().filter(c -> c.getId().equals(id))
                    .findAny();
            if (contractor_to_check.isPresent()) {
                contractorRepository.deleteById(id);
                user.getContractors().remove(contractor_to_check.get());
                userRepository.save(user);
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Contractor> getContractorWithId(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Contractor> contractor_to_get = user.getContractors().stream().filter(c -> c.getId().equals(id))
                    .findAny();
            if (contractor_to_get.isPresent()) {
                return new ResponseEntity<>(contractor_to_get.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    
    @Override
    public ResponseEntity<List<Contractor>> getContractors(String searchstr) {
        User user = userService.getUser();
        if (user != null) {
            // return all if searchstr empty
            if (searchstr == " " | searchstr.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<Contractor>(user.getContractors()), HttpStatus.OK);
            }
            // filter products with searchstr
            List<Contractor> foundContractors = user.getContractors().stream().filter(c -> c.getName().contains(searchstr))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(foundContractors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<InlineResponse201> newContractor(Contractor contractor) {
        User user = userService.getUser();
        if (user != null) {
            Set<Contractor> contractors = user.getContractors();
            if (!contractors.isEmpty()) {
                Optional<Contractor> product_to_check = user.getContractors().stream()
                        .filter(c -> c.getName().equals(contractor.getName())).findAny();
                if (product_to_check.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            InlineResponse201 res = new InlineResponse201();
            res.setId(contractorRepository.save(contractor).getId());
            contractors.add(contractor);
            userRepository.save(user);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> updateContractor(String id, Contractor contractor) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Contractor> contractor_to_update = user.getContractors().stream().filter(c -> c.getId().equals(id))
                    .findAny();
            if (contractor_to_update.isPresent()) {
                contractor.setId(id);
                contractorRepository.save(contractor);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
