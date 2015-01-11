package com.flexible.order.testhelper.EntityBuilder;

import com.flexible.order.domain.report.ShippingItem;

public class ShippingItemBuilder extends ReportItemBuilder<ShippingItem, Builder<ShippingItem>>{

	@Override
	public ShippingItem build() {
		ShippingItem shippingItem = new ShippingItem();
		super.build(shippingItem);
		return shippingItem;
	}

}