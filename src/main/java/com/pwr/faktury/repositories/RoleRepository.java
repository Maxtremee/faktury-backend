package com.pwr.faktury.repositories;

import java.util.Optional;

import com.pwr.faktury.models.ERole;
import com.pwr.faktury.models.Role;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(@Param("name") ERole name);
}
