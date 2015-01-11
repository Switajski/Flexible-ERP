package com.flexible.order.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.Product;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.reference.ProductType;
import com.flexible.order.service.QuantityLeftCalculatorService;
import com.flexible.order.testhelper.EntityBuilder.AddressBuilder;
import com.flexible.order.testhelper.EntityBuilder.CatalogProductBuilder;
import com.flexible.order.testhelper.EntityBuilder.ConfirmationItemBuilder;
import com.flexible.order.testhelper.EntityBuilder.CustomerBuilder;
import com.flexible.order.testhelper.EntityBuilder.DeliveryNotesBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderItemBuilder;
import com.flexible.order.testhelper.EntityBuilder.ShippingItemBuilder;

public class QuantityCalculatorTest {

    private static final int QTY_PROCESSED = 5;
    private static final int QTY = 7;
    private OrderItem orderItem;
    private Address address = AddressBuilder.createDefault();

    private QuantityLeftCalculatorService calcService = new QuantityLeftCalculatorService();

    @Test
    public void toBeConfirmed_qtyLeftShouldBeQtyMinusQtyProcessed() {
        // GIVEN
        orderItem = givenOrderItem(QTY);
        orderItem.addReportItem(givenAgreedItem(QTY_PROCESSED));

        // WHEN
        Integer calculatedQuantity = calcService.calculateLeft(orderItem);

        // THEN
        assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
    }

    @Test
    public void toBeAgreed_qtyLeftShouldBeQtyMinusQtyProcessed() {
        // GIVEN
        orderItem = givenOrderItem(QTY);
        orderItem.addReportItem(givenAgreedItem(QTY));
        orderItem.addReportItem(givenDeliveryItem(QTY_PROCESSED));

        // WHEN
        Integer calculatedQuantity = calcService.calculateLeft(orderItem.getConfirmationItems().iterator().next());

        // THEN
        assertThat(calculatedQuantity, is(QTY - QTY_PROCESSED));
    }

    private ConfirmationItem givenAgreedItem(int quantityProcessed) {
        return new ConfirmationItemBuilder()
                .setReport(givenConfirmationReport())
                .setQuantity(quantityProcessed)
                .setItem(orderItem)
                .build();
    }

    private ReportItem givenDeliveryItem(int qtyProcessed) {
        return new ShippingItemBuilder()
                .setReport(givenDeliveryNotes())
                .setQuantity(qtyProcessed)
                .setItem(orderItem)
                .build();
    }

    private Report givenDeliveryNotes() {
        return new DeliveryNotesBuilder().setDocumentNumber("L-123").build();
    }

    private OrderItem givenOrderItem(int quantity) {
        return new OrderItemBuilder(
                givenOrder(),
                givenProduct(),
                quantity)
                .build();
    }

    private OrderConfirmation givenConfirmationReport() {
        OrderConfirmation orderConfirmation = new OrderConfirmation("AB-123", address, address);
        orderConfirmation.setOrderAgreementNumber("AU-123");
        return orderConfirmation;
    }

    private Product givenProduct() {
        return new CatalogProductBuilder(
                "pro",
                "234",
                ProductType.PRODUCT)
                .build().toProduct();
    }

    private Order givenOrder() {
        return new OrderBuilder()
                .setCustomer(CustomerBuilder.buildWithGeneratedAttributes(2))
                .build();
    }
}
