package com.pwr.faktury.models.adapters;

import com.pwr.faktury.model.Invoice;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invoices")
public class InvoiceAdapter extends Invoice {
    Invoice invoice;

    public InvoiceAdapter(Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice get() {
        return this.invoice;
    }
    
}
