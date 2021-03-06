package com.flexible.order.service;

import org.springframework.stereotype.Service;

import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.PurchaseAgreement;

@Service
public class InvoicingAddressService extends AddressFromPurchaseAgreementRetriever{

    @Override
    Address getAddress(PurchaseAgreement purchaseAgreement) {
        return purchaseAgreement.getInvoiceAddress();
    }
    
}
