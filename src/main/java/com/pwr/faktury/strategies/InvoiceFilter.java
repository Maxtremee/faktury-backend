package com.pwr.faktury.strategies;

import java.time.LocalDate;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import com.pwr.faktury.model.Invoice;

public interface InvoiceFilter extends UnaryOperator<List<Invoice>> {
    default InvoiceFilter combine(InvoiceFilter after) {
        return value -> after.apply(this.apply(value));
    }

    static InvoiceFilter contractor(String contractor) {
        return (invoices) -> invoices.stream().filter(i -> i.getContractor().getName().equals(contractor)).collect(Collectors.toList());
    }

    static InvoiceFilter before(LocalDate before) {
        return (invoices) -> invoices.stream().filter(i -> i.getIssueDate().isBefore(before)).collect(Collectors.toList());
    }

    static InvoiceFilter after(LocalDate after) {
        return (invoices) -> invoices.stream().filter(i -> i.getIssueDate().isAfter(after)).collect(Collectors.toList());
    }
}
