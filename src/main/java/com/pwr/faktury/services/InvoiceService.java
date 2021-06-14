package com.pwr.faktury.services;

import java.io.ByteArrayInputStream;

import com.pwr.faktury.model.Address;
import com.pwr.faktury.model.Contractor;
import com.pwr.faktury.model.Invoice;
import com.pwr.faktury.model.InvoiceItem;
import com.pwr.pdf.PdfInvoice;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
        public ByteArrayInputStream generatePdfFromId(Contractor seller, Invoice invoice) {
                Address buyerAddress = invoice.getContractor().getAddress();
                Address sellerAddress = seller.getAddress();
                PdfInvoice pdfInvoice = new PdfInvoice();

                pdfInvoice.setTitle(str(invoice.getTitle()));
                pdfInvoice.setBuyerData(
                                str(invoice.getContractor().getName()),
                                str(buyerAddress.getStreet()) + ", " + str(buyerAddress.getPostalCode()) + " " + str(buyerAddress.getCity()),
                                "tel. " + str(invoice.getContractor().getPhoneNumber()) + ", email: " + str(invoice.getContractor().getEmail()),
                                str(invoice.getContractor().getNip()));
                pdfInvoice.setSellerData(
                                str(seller.getName()),
                                str(sellerAddress.getStreet()) + ", " + str(sellerAddress.getPostalCode()) + " " + str(sellerAddress.getCity()),
                                "tel. " + str(seller.getPhoneNumber()) + " , email: " + str(seller.getEmail()),
                                str(seller.getNip()));
                pdfInvoice.setInfoData(
                                str(invoice.getIssuePlace()), 
                                str(invoice.getIssueDate()),
                                str(invoice.getSellDate()));
                pdfInvoice.setPaymentDetails(
                                str(invoice.getPaymentType()),
                                str(invoice.getPaymentDate()),
                                str(seller.getBankName()), 
                                str(seller.getBankAccountNumber()));
                for (InvoiceItem item : invoice.getProducts()) {
                        pdfInvoice.addProduct(
                                str(item.getName()),
                                item.getQuantity().floatValue(),
                                str(item.getUnit()),
                                item.getPrice().floatValue(),
                                item.getTax());
                }
                return pdfInvoice.createInvoice();
        }

        private String str(Object o) {
                try {
                        return o != null ? o.toString() : "";
                } catch (NullPointerException e) {
                        return "";
                }
        }
}
