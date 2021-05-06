package com.pwr.faktury.repositories;

import java.util.Optional;

import com.pwr.faktury.model.ERole;
import com.pwr.faktury.model.Role;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(@Param("name") ERole name);
}
