package com.flexible.order.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import com.flexible.order.domain.OrderItem;

@Entity
public class ShippingItem extends ReportItem {

	public ShippingItem() {
	}

	public ShippingItem(DeliveryNotes deliveryNotes,
			OrderItem orderItemToBeDelivered, Integer quantityToDeliver,
			Date date) {
		super(
				deliveryNotes,
				orderItemToBeDelivered,
				quantityToDeliver,
				date);
	}

	@Override
	public String provideStatus() {
		return "geliefert";
	}

	public DeliveryNotes getDeliveryNotes() {
		return (DeliveryNotes) this.report;
	}

}
