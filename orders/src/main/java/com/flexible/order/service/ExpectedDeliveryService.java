package com.flexible.order.service;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.ReportItem;

@Service
public class ExpectedDeliveryService {

	@Autowired
	PurchaseAgreementService purchaseAgreementService;
	
	public Set<LocalDate> retrieve(Set<ReportItem> items) {
		Set<LocalDate> expectedDeliveryDates = new HashSet<LocalDate>();
		for (PurchaseAgreement pa : purchaseAgreementService.retrieve(items)){
			expectedDeliveryDates.add(pa.getExpectedDelivery());
		}
		return expectedDeliveryDates;
	}

}
