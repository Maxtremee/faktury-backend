package com.pwr.faktury.repositories;

import java.util.Optional;

import com.pwr.faktury.model.Invoice;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    Optional<Invoice> findByTitle(@Param("title") String title);
}
