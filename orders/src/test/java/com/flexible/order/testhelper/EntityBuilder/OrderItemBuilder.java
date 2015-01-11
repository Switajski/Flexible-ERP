package com.flexible.order.testhelper.EntityBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.flexible.order.domain.CatalogProduct;
import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.Product;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.reference.Currency;
import com.flexible.order.reference.OriginSystem;

public class OrderItemBuilder implements Builder<OrderItem> {

	private Set<ReportItem> reportItems = new HashSet<ReportItem>();
	private Integer orderedQuantity;
	private Amount negotiatedPriceNet;
	private Product product;
	private String packageNumber;
	private String trackingNumber;
	private Order order;

	public OrderItemBuilder(Order order, Product product, int orderedQuantity) {
		this.order = order;
		this.product = product;
		this.orderedQuantity = orderedQuantity;
	}

	public OrderItemBuilder() {
	}

	@Override
	public OrderItem build() {
		OrderItem item = new OrderItem(order, product, orderedQuantity);
		item.setReportItems(reportItems);
		item.setNegotiatedPriceNet(negotiatedPriceNet);
		item.setPackageNumber(packageNumber);
		item.setTrackingNumber(trackingNumber);
		return item;
	}

	public OrderItemBuilder generateAttributes(Integer i) {
		orderedQuantity = i;
		negotiatedPriceNet = new Amount(new BigDecimal(i), Currency.EUR);
		packageNumber = i.toString();
		trackingNumber = i.toString();
		return this;
	}

	public static OrderItem buildWithGeneratedAttributes(Integer i) {
		return new OrderItemBuilder(
				new Order(
						"",
						OriginSystem.FLEXIBLE_ORDERS,
						i.toString()),
				CatalogProductBuilder
						.buildWithGeneratedAttributes(i)
						.toProduct(),
				i)
				.generateAttributes(i).build();
	}

	public OrderItemBuilder setReportItems(Set<ReportItem> reportItems) {
		this.reportItems = reportItems;
		return this;
	}

	public OrderItemBuilder setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
		return this;
	}

	public OrderItemBuilder setNegotiatedPriceNet(Amount negotiatedPriceNet) {
		this.negotiatedPriceNet = negotiatedPriceNet;
		return this;
	}

	public OrderItemBuilder setProduct(Product product) {
		this.product = product;
		return this;
	}

	public OrderItemBuilder setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
		return this;
	}

	public OrderItemBuilder setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
		return this;
	}

	public OrderItemBuilder setOrder(Order order) {
		this.order = order;
		return this;
	}

	public OrderItemBuilder addHandlingEvent(ReportItem reportItem) {
		this.reportItems.add(reportItem);
		return this;
	}

	public static OrderItem build(CatalogProduct catalogProduct, int quantity) {
		OrderItem oi = new OrderItemBuilder(
				null,
				catalogProduct.toProduct(),
				quantity).build();
		return oi;
	}

}
