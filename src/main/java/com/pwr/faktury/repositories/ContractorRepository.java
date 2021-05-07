package com.pwr.faktury.repositories;

import com.pwr.faktury.model.Contractor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRepository extends MongoRepository<Contractor, String> {
    
}
