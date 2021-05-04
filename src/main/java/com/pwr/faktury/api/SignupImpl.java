package com.pwr.faktury.api;

import com.pwr.faktury.model.Signup;
import com.pwr.faktury.model.User;
import com.pwr.faktury.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SignupImpl implements SignupApiDelegate {
    private UserRepository userRepository;

    @Autowired
    public SignupImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Void> signup(Signup signup) {
        if (signup != null) {
            if (userRepository.existsByLogin(signup.getLogin())) {
                return new ResponseEntity<Void>(HttpStatus.CONFLICT);
            };
            User user = new User();
            user.setLogin(signup.getLogin());
            user.setPersonal_data(signup.getCompany());
            userRepository.save(user);

            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
