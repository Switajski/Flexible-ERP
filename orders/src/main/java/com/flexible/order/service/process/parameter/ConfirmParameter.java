package com.flexible.order.service.process.parameter;

import java.util.List;

import org.joda.time.LocalDate;

import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.CustomerDetails;
import com.flexible.order.web.dto.ItemDto;

public class ConfirmParameter {
	public String orderNumber;
	public String confirmNumber;
	public LocalDate expectedDelivery;
	public Long deliveryMethodNo;
	public Address shippingAddress;
	public Address invoiceAddress;
	public List<ItemDto> orderItems;
	public CustomerDetails customerDetails;
    public Long customerNumber;

	public ConfirmParameter(String orderNumber, String confirmNumber,
			LocalDate expectedDelivery, Long deliveryMethodNoNumber, Address shippingAddress,
			Address invoiceAddress, List<ItemDto> orderItems) {
		this.orderNumber = orderNumber;
		this.confirmNumber = confirmNumber;
		this.expectedDelivery = expectedDelivery;
		this.deliveryMethodNo = deliveryMethodNoNumber;
		this.shippingAddress = shippingAddress;
		this.invoiceAddress = invoiceAddress;
		this.orderItems = orderItems;
	}

	public ConfirmParameter() {
	}

}