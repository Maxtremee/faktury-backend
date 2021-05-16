package com.pwr.faktury.controllers;

import com.pwr.faktury.api.LogoutApiDelegate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LogoutImpl implements LogoutApiDelegate{
    @Override
    public ResponseEntity<Void> logout() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }
}
