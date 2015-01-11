package com.flexible.order.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.ShippingItem;

public class ShippingCostsCalculator {

	public Amount calculate(Set<ShippingItem> shippingItems) {
		Amount summedShippingCosts = new Amount();
		for (DeliveryNotes dn : retrieveDeliveryNotesWithShippingCosts(shippingItems)) {
			summedShippingCosts = summedShippingCosts
					.add(dn.getShippingCosts());
		}
		return summedShippingCosts;
	}

	private Set<DeliveryNotes> retrieveDeliveryNotesWithShippingCosts(
			Set<ShippingItem> shippingItems) {
		// using HashMap with documentNumber to not sum up shipping costs from
		// same document
		HashMap<String, DeliveryNotes> deliveryNotes = new HashMap<String, DeliveryNotes>();

		for (ShippingItem entry : shippingItems) {
			for (ShippingItem shippingItem : entry.getOrderItem().getShippingItems()) {
				DeliveryNotes dn = shippingItem.getDeliveryNotes();
				if (dn.getDocumentNumber() == null)
					throw new IllegalStateException("No Documentno. set");
				if (dn.hasShippingCosts())
					deliveryNotes.put(dn.getDocumentNumber(), dn);
			}
		}
		return new HashSet<DeliveryNotes>(deliveryNotes.values());
	}
}
