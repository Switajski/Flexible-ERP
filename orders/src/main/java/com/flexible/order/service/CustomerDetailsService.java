package com.flexible.order.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.DeliveryHistory;
import com.flexible.order.domain.embeddable.CustomerDetails;
import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;

@Service
public class CustomerDetailsService {

	@Autowired
	PurchaseAgreementService purchaseAgreementService;

    private Set<CustomerDetails> retrieve(ReportItem reportItem) {
        Set<CustomerDetails> customerDetailss = new HashSet<CustomerDetails>();
        DeliveryHistory dh = DeliveryHistory.of(reportItem.getOrderItem());
        for (ConfirmationItem ai : dh.getReportItems(ConfirmationItem.class)) {
            try {
                CustomerDetails customerDetails = ((OrderConfirmation) ai.getReport()).getCustomerDetails();
                customerDetailss.add(customerDetails);
            }
            catch (ClassCastException e) {
                throw new RuntimeException("System expected, that an AgreementItem has an OrderAgreement as Report", e);
            }
        }
        return customerDetailss;
    }

    @Transactional(readOnly = true)
    public Set<CustomerDetails> retrieve(Set<ReportItem> reportItems){
        Set<CustomerDetails> customerDetails = new HashSet<CustomerDetails>();
        for (ReportItem ri:reportItems){
            Report r = ri.getReport();
            if (r instanceof OrderConfirmation)
                customerDetails.add(((OrderConfirmation) r).getCustomerDetails());
        }
            
        for (ReportItem ri:reportItems){
            customerDetails.addAll(retrieve(ri));
        }
        return customerDetails; 
    }

}
