package com.flexible.order.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.DeliveryHistory;
import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.ReportItem;

@Service
public class PurchaseAgreementService {

    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> retrieve(Collection<ReportItem> reportItems) {
        DeliveryHistory dh = new DeliveryHistory(reportItems);
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>();
        for (ConfirmationItem cis : dh.getReportItems(ConfirmationItem.class)) {
            OrderConfirmation orderConfirmation = (OrderConfirmation) cis.getReport();
            ocs.add(orderConfirmation.getPurchaseAgreement());
        }
        return ocs;
    }

    /**
     * retrieve only purchase agreements that are agreed by an order agreement
     * 
     * @param reportItems
     * @return
     */
    @Transactional(readOnly = true)
    public Set<PurchaseAgreement> retrieveLegal(Collection<ReportItem> reportItems) {
        DeliveryHistory dh = new DeliveryHistory(reportItems);
        Set<PurchaseAgreement> ocs = new HashSet<PurchaseAgreement>();
        for (ConfirmationItem cis : dh.getReportItems(ConfirmationItem.class)) {
            OrderConfirmation orderConfirmation = (OrderConfirmation) cis.getReport();
            if (orderConfirmation.isAgreed()) {
                ocs.add(orderConfirmation.getPurchaseAgreement());
            }
        }
        return ocs;
    }

}
