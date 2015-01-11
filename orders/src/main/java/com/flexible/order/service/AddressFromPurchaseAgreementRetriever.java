package com.flexible.order.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.DeliveryHistory;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.ReportItem;

public abstract class AddressFromPurchaseAgreementRetriever {

    abstract Address getAddress(PurchaseAgreement purchaseAgreement);

    @Transactional(readOnly = true)
    public Set<Address> retrieve(ReportItem reportItem) {
        Set<Address> addresses = new HashSet<Address>();
        if (reportItem.getReport() instanceof OrderConfirmation) {
            // a purchase agreement only exists after OrderAgreement  
            addresses.add(getAddress(getPurchaseAgreementFromOrderConfirmation(reportItem)));
        }
        else {
            DeliveryHistory dh = DeliveryHistory.of(reportItem.getOrderItem());
            for (ConfirmationItem ai : dh.getReportItems(ConfirmationItem.class)) {
                try {
                    PurchaseAgreement purchaseAgreement = ((OrderConfirmation) ai.getReport()).getPurchaseAgreement();
                    addresses.add(getAddress(purchaseAgreement));
                }
                catch (ClassCastException e) {
                    throw new RuntimeException("AgreementItem expected to have OrderAgreement as Report", e);
                }
            }
        }
        return addresses;
    }

    private PurchaseAgreement getPurchaseAgreementFromOrderConfirmation(ReportItem reportItem) {
        return ((OrderConfirmation) reportItem.getReport()).getPurchaseAgreement();
    }

    @Transactional(readOnly = true)
    public Set<Address> retrieve(Set<ReportItem> reportItems) {
        Set<Address> addresses = new HashSet<Address>();
        for (ReportItem ri : reportItems) {
            addresses.addAll(retrieve(ri));
        }
        return addresses;
    }

}
