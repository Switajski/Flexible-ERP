package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ShippingItem extends ReportItem {
	
	protected ShippingItem() {}

	public ShippingItem(DeliveryNotes deliveryNotes, ReportItemType handlingEventType,
			OrderItem orderItemToBeDelivered, Integer quantityToDeliver,
			Date date) {
		super(deliveryNotes, handlingEventType, orderItemToBeDelivered, quantityToDeliver, date);
	}

}
