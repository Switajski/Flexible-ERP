package com.flexible.order.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import com.flexible.order.domain.OrderItem;

@Entity
public class CancellationItem extends ReportItem {

	protected CancellationItem() {
	}

	public CancellationItem(CancelReport cancelReport,
			OrderItem orderItem, int quantity, Date date) {
		super(cancelReport, orderItem, quantity, date);
	}

	@Override
	public String provideStatus() {
		return "storniert";
	}

}
