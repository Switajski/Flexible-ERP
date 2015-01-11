package com.flexible.order.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.service.CustomerDetailsService;
import com.flexible.order.service.PurchaseAgreementService;
import com.flexible.order.service.ShippingAddressService;
import com.flexible.order.web.dto.ReportDto;

@Service
public class DeliveryNotesToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;
	@Autowired
	ShippingAddressService shippingAddressService;
	@Autowired
	CustomerDetailsService customerDetailsService;
	@Autowired
	PurchaseAgreementService purchaseAgreementService;

	@Transactional(readOnly = true)
	public ReportDto toDto(DeliveryNotes report) {
		ReportDto dto = reportToDtoConversionService.toDto(report);
		return dto;
	}

}
