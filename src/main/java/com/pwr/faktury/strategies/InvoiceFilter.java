package com.pwr.faktury.strategies;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.pwr.faktury.model.Invoice;

public interface InvoiceFilter extends UnaryOperator<Set<Invoice>> {
    default InvoiceFilter combine(InvoiceFilter after) {
        return value -> after.apply(this.apply(value));
    }

    static InvoiceFilter title(String title) {
        return (invoices) -> invoices.stream().filter(i -> i.getTitle().contains(title)).collect(Collectors.toSet());
    }

    static InvoiceFilter contractor(String contractor) {
        return (invoices) -> invoices.stream().filter(i -> i.getContractor().getName().contains(contractor)).collect(Collectors.toSet());
    }

    static InvoiceFilter before(LocalDate before) {
        return (invoices) -> invoices.stream().filter(i -> i.getIssueDate().isBefore(before)).collect(Collectors.toSet());
    }

    static InvoiceFilter after(LocalDate after) {
        return (invoices) -> invoices.stream().filter(i -> i.getIssueDate().isAfter(after)).collect(Collectors.toSet());
    }
}
