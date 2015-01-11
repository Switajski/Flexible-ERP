package com.flexible.order.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import com.flexible.order.domain.OrderItem;

@Entity
public class InvoiceItem extends ReportItem {

	public InvoiceItem() {
	}

	public InvoiceItem(Invoice invoice, 
			OrderItem orderItem, Integer quantityToDeliver, Date date) {
		super(invoice, orderItem, quantityToDeliver, date);
	}

	@Override
	public String provideStatus() {
		return "in Rechnung gestellt";
	}

}
