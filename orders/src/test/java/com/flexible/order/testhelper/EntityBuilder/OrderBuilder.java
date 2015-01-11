package com.flexible.order.testhelper.EntityBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.flexible.order.domain.Customer;
import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.reference.OriginSystem;

public class OrderBuilder {

	private String orderNumber;
	private Customer customer;
	private String customerEmail;
	private OriginSystem originSystem;
	private Set<OrderItem> items = new HashSet<OrderItem>();

	public Order build() {
		Order order = new Order();
		order.setOrderNumber(orderNumber);
		order.setCustomer(customer);
		order.setCustomerEmail(customerEmail);
		order.setOriginSystem(originSystem);
		for (OrderItem item:items)
			order.addOrderItem(item);
		return order;
	}
	
	public static Order B11(){
		return new OrderBuilder().withB11().build();
	}

	public OrderBuilder withB11() {
		Customer yvonne = new CustomerBuilder().yvonne().build();
		this.setOrderNumber("B11")
		.setCustomer(yvonne)
		.setItems(new HashSet<OrderItem>(Arrays.asList(
					OrderItemBuilder.build(new CatalogProductBuilder().amy().build(), 10),
					OrderItemBuilder.build(new CatalogProductBuilder().miladka().build(), 15),
					OrderItemBuilder.build(new CatalogProductBuilder().paul().build(), 30))))
		.setCustomerEmail(yvonne.getEmail())
		.setOriginSystem(OriginSystem.FLEXIBLE_ORDERS);
		return this;
	}
	
	public static Order B12(){
		return new OrderBuilder().withB12().build();
	}
	
	public OrderBuilder withB12() {
		Customer yvonne = new CustomerBuilder().yvonne().build();
		this.setOrderNumber("B12")
		.setCustomer(yvonne)
		.setItems(new HashSet<OrderItem>(Arrays.asList(
					OrderItemBuilder.build(new CatalogProductBuilder().salome().build(), 12),
					OrderItemBuilder.build(new CatalogProductBuilder().jurek().build(), 5))))
		.setCustomerEmail(yvonne.getEmail())
		.setOriginSystem(OriginSystem.FLEXIBLE_ORDERS);
		return this;
	}

	public OrderBuilder setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
		return this;
	}

	public OrderBuilder setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public OrderBuilder setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
		return this;
	}

	public OrderBuilder setOriginSystem(OriginSystem originSystem) {
		this.originSystem = originSystem;
		return this;
	}

	public OrderBuilder setItems(Set<OrderItem> items) {
		this.items = items;
		return this;
	}

	public OrderBuilder addOrderItem(OrderItem orderItem) {
		this.items.add(orderItem);
		return this;
	}

}
