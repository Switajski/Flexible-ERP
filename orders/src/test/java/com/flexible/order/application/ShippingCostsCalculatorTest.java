package com.flexible.order.application;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.junit.Test;

import com.flexible.order.application.ShippingCostsCalculator;
import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.ShippingItem;
import com.flexible.order.reference.Currency;
import com.flexible.order.testhelper.EntityBuilder.DeliveryNotesBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderBuilder;

public class ShippingCostsCalculatorTest {

	private static Amount shippingCosts = new Amount(
			BigDecimal.TEN,
			Currency.EUR);
	private static Amount shippingCosts2 = new Amount(
			BigDecimal.valueOf(4.5d),
			Currency.EUR);

	@Test
	public void calculate_AmountShouldBeSummedShippingCosts() {
		// GIVEN
		Order order = OrderBuilder.B11();
		DeliveryNotes l12 = givenDeliveryNotesL12(order);
		DeliveryNotes l13 = givenDeliveryNotesL13(order);

		// WHEN
		Amount calculatedCosts = new ShippingCostsCalculator().calculate(
				new HashSet<ShippingItem>(
						Arrays.asList(
								l12.getShippingItems().iterator().next(),
								l13.getShippingItems().iterator().next()))
				);

		// THEN
		assertThat(calculatedCosts, equalTo(shippingCosts.add(shippingCosts2)));
	}

	private DeliveryNotes givenDeliveryNotesL13(Order order) {
		DeliveryNotes l13 = new DeliveryNotesBuilder()
		.setShippingCosts(shippingCosts2)
		.setDocumentNumber("L13")
		.build();
		for (OrderItem oi : order.getItems()) {
			oi.addReportItem(new ShippingItem(
					l13,
					oi,
					5,
					new Date()));
		}
		return l13;
	}

	private DeliveryNotes givenDeliveryNotesL12(Order order) {
		DeliveryNotes l12 = new DeliveryNotesBuilder()
			.setShippingCosts(shippingCosts)
			.setDocumentNumber("L12")
			.build();
		for (OrderItem oi : order.getItems()) {
			oi.addReportItem(new ShippingItem(
					l12,
					oi,
					5,
					new Date()));
		}
		return l12;
	}

}
