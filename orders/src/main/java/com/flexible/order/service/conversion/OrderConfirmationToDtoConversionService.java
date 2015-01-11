package com.flexible.order.service.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.service.CustomerDetailsService;
import com.flexible.order.service.InvoicingAddressService;
import com.flexible.order.web.dto.ReportDto;

@Service
public class OrderConfirmationToDtoConversionService {

	@Autowired
	ReportToDtoConversionService reportToDtoConversionService;
	@Autowired
	InvoicingAddressService invocingAddressService;
	@Autowired
	CustomerDetailsService customerDetailsService;

	@Transactional(readOnly = true)
	public ReportDto toDto(OrderConfirmation orderConfirmation) {
		ReportDto dto = reportToDtoConversionService.toDto(orderConfirmation);
		if (orderConfirmation.isAgreed())
		    dto.orderConfirmationNumber = orderConfirmation.getOrderAgreementNumber();
		return dto;
	}

}
