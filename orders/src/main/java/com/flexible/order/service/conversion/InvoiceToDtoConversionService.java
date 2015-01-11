package com.flexible.order.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.report.Invoice;
import com.flexible.order.service.CustomerDetailsService;
import com.flexible.order.service.InvoicingAddressService;
import com.flexible.order.web.dto.ReportDto;

@Service
public class InvoiceToDtoConversionService {

	@Autowired
	private InvoicingAddressService invocingAddressService;

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;

	@Autowired
	CustomerDetailsService customerDetailsService;

	@Transactional(readOnly = true)
	public ReportDto toDto(Invoice report) {
		ReportDto dto = reportToDtoConversionService.toDto(report);
		dto.shippingSpecific_shippingCosts = report.getShippingCosts();
		dto.invoiceSpecific_paymentConditions = report.getPaymentConditions();
		dto.invoiceSpecific_billing = report.getBilling();
		return dto;
	}

}
