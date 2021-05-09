package com.pwr.faktury.repositories;

import java.util.Optional;

import com.pwr.faktury.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByLogin(@Param("login") String login);
    
    Boolean existsByLogin(@Param("login") String login);
    
    @Query("{ '_id': ObjectId('?0'), 'products.$id': ObjectId('?1') }")
    User productExistsById(String userId, String productId);

    @Query("{'contractors.$id': ?0 }")
    Boolean contractorExistsById(String id);

    @Query("{'invoices.$id': ?0 }")
    Boolean invoiceExistsById(String id);

    @Query("{ '$pull': { 'products': { '$id' : ?0 } } }")
    Void deleteProductWithId(String id);
}
