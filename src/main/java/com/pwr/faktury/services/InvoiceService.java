package com.pwr.faktury.services;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import com.pwr.faktury.model.Contractor;
import com.pwr.faktury.model.Invoice;
import com.pwr.faktury.model.InvoiceItem;
import com.pwr.pdf.PdfInvoice;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    public ByteArrayInputStream generatePdfFromId(Contractor seller, Invoice invoice) {
        PdfInvoice pdfInvoice = new PdfInvoice();
        
        pdfInvoice.setTitle(str(invoice.getTitle()));
        pdfInvoice.setBuyerData(str(invoice.getContractor()
                .getName()), str(
                        invoice.getContractor()
                                .getFirstAddressLine()),
                str(invoice.getContractor().getSecondAddressLine()),str(invoice.getContractor().getNip()));
        pdfInvoice.setSellerData(str(
                seller.getName()), str(
                        seller.getFirstAddressLine()), str(seller.getSecondAddressLine()),
                str(seller.getNip()));
        pdfInvoice.setInfoData(str(invoice.getIssuePlace()), str(
                invoice.getIssueDate().toString()), str(invoice.getSellDate()));
        pdfInvoice.setPaymentDetails(str(invoice
                .getPaymentType()), str(
                        invoice.getPaymentDate().toString()),
                str(seller.getBankName()), str(seller.getBankAccountNumber()));
        for (InvoiceItem item : invoice.getProducts()) {
            pdfInvoice.addProduct(str(item.getName()), item.getQuantity().floatValue(), str(item.getUnit()),
                    item.getPrice().floatValue(), item.getTax());
        }
        
        return pdfInvoice.createInvoice();
    }

    private String str(String str) {
        return str != null ? str : "";
    }
}
