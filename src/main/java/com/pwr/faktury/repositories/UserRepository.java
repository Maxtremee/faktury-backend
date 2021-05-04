package com.pwr.faktury.repositories;

import java.util.UUID;

import com.pwr.faktury.model.Login;
import com.pwr.faktury.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    Boolean existsByLogin(@Param("login") Login login);
}
