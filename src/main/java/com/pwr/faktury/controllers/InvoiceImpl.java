package com.pwr.faktury.controllers;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.pwr.faktury.api.InvoiceApiDelegate;
import com.pwr.faktury.model.Invoice;
import com.pwr.faktury.models.User;
import com.pwr.faktury.repositories.InvoiceRepository;
import com.pwr.faktury.repositories.UserRepository;
import com.pwr.faktury.security.services.UserService;
import com.pwr.faktury.services.InvoiceService;
import com.pwr.faktury.strategies.InvoiceFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InvoiceImpl implements InvoiceApiDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public ResponseEntity<Void> deleteInvoice(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Invoice> invoice_to_check = user.getInvoices().stream().filter(i -> i.getId().equals(id))
                    .findAny();
            if (invoice_to_check.isPresent()) {
                invoiceRepository.deleteById(id);
                user.getInvoices().remove(invoice_to_check.get());
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
    public ResponseEntity<Invoice> getInvoice(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Invoice> invoice_to_get = user.getInvoices().stream().filter(i -> i.getId().equals(id))
                    .findAny();
            if (invoice_to_get.isPresent()) {
                return new ResponseEntity<>(invoice_to_get.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getInvoicePdf(String id) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Invoice> invoice_to_get = user.getInvoices().stream().filter(i -> i.getId().equals(id))
                    .findAny();
            if (invoice_to_get.isPresent()) {
                ByteArrayInputStream bis = invoiceService.generatePdfFromId(user.getPersonal_data(),
                        invoice_to_get.get());
                if (bis == null) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Content-Disposition", "inline; filename=" + invoice_to_get.get().getTitle().replaceAll("/", "-") + ".pdf");
                return new ResponseEntity<>(new InputStreamResource(bis), responseHeaders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }    
    }

    @Override
    public ResponseEntity<List<Invoice>> getInvoices(String title,
    Integer quantity,
    LocalDate before,
    LocalDate after,
    String contractor) {
        User user = userService.getUser();
        if (user != null) {
            Set<Invoice> invoices = user.getInvoices();
            Set<InvoiceFilter> filters = new HashSet<>();
            if (title != null) {
                filters.add(InvoiceFilter.title(title));
            }
            if (before != null) {
                filters.add(InvoiceFilter.before(before));
            }
            if (after != null) {
                filters.add(InvoiceFilter.after(after));
            }
            if (contractor != null) {
                filters.add(InvoiceFilter.contractor(contractor));
            }
            final InvoiceFilter combinedFilters = filters.stream().reduce(i -> i, InvoiceFilter::combine);
            invoices = combinedFilters.apply(invoices);
            return new ResponseEntity<>(new ArrayList<>(invoices), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getInvoicesPdf(String name,
    Integer quantity,
    LocalDate before,
    LocalDate after,
    String contractor) {
        //TODO: implement
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> newInvoice(Invoice invoice) {
        User user = userService.getUser();
        if (user != null) {
            Set<Invoice> invoices = user.getInvoices();
            if (!invoices.isEmpty()) {
                Optional<Invoice> invoice_to_check = user.getInvoices().stream()
                        .filter(i -> i.getTitle().equals(invoice.getTitle())).findAny();
                if (invoice_to_check.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            invoiceRepository.save(invoice);
            invoices.add(invoice);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<Void> updateInvoice(String id,
    Invoice invoice) {
        User user = userService.getUser();
        if (user != null) {
            Optional<Invoice> invoice_to_update = user.getInvoices().stream().filter(i -> i.getId().equals(id))
                    .findAny();
            if (invoice_to_update.isPresent()){
                invoice.setId(id);
                invoice.setEdited(LocalDateTime.now());
                invoiceRepository.save(invoice);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
