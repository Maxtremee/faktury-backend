package com.pwr.faktury.security.services;

import java.io.ByteArrayInputStream;

import com.pwr.faktury.model.Contractor;
import com.pwr.faktury.model.Invoice;
import com.pwr.faktury.model.InvoiceItem;
import com.pwr.pdf.PdfInvoice;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    public ByteArrayInputStream generatePdfFromId(Contractor seller, Invoice invoice){
        PdfInvoice pdfInvoice = new PdfInvoice();
        pdfInvoice.setBuyerData(invoice.getContractor().getName(), invoice.getContractor().getFirstAddressLine(), invoice.getContractor().getSecondAddressLine(), invoice.getContractor().getNip());
        pdfInvoice.setSellerData(seller.getName(), seller.getFirstAddressLine(), seller.getSecondAddressLine(), seller.getNip());
        pdfInvoice.setInfoData(invoice.getIssuePlace(), invoice.getIssueDate().toString(), invoice.getSellDate());
        pdfInvoice.setPaymentDetails(invoice.getInfo(), invoice.getDueDate().toString(), invoice.getContractor().getBankName(), invoice.getContractor().getBankAccountNumber());
        for(InvoiceItem item : invoice.getProducts()){
            pdfInvoice.addProduct(item.getName(), item.getQuantity().floatValue(), item.getUnit(), item.getPrice().floatValue(), item.getTax());
        }
        return pdfInvoice.createInvoice();
    }
}
