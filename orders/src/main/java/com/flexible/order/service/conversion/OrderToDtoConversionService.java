package com.flexible.order.service.conversion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.AmountCalculator;
import com.flexible.order.application.BusinessConstants;
import com.flexible.order.domain.Customer;
import com.flexible.order.domain.Order;
import com.flexible.order.web.dto.ReportDto;

@Service
public class OrderToDtoConversionService {
	
	@Transactional(readOnly=true)
	public ReportDto toDto(Order order){
		ReportDto dto = new ReportDto();
		dto.created = order.getCreated();
		Customer customer = order.getCustomer();
		dto.customerNumber = customer.getCustomerNumber();
		dto.documentNumber = order.getOrderNumber();
		dto.customerFirstName = customer.getFirstName();
		dto.customerLastName = customer.getLastName();
		dto.customerEmail = customer.getEmail();
		dto.customerPhone = customer.getPhone();
		dto.headerAddress = BusinessConstants.MY_ADDRESS;
		dto.shippingSpecific_shippingAddress = customer.getShippingAddress();
		dto.orderItems = order.getItems(); 
		dto.netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity2(order.getItems()));
		dto.vatRate = order.getVatRate();
		return dto;
	}
	
}
