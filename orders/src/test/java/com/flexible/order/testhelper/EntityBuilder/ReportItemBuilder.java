package com.flexible.order.testhelper.EntityBuilder;

import java.util.Date;

import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;

public abstract class ReportItemBuilder <BEAN extends ReportItem, BUILDER extends Builder<BEAN>> implements Builder<BEAN> {

	protected Integer quantity;
	protected Report report = null;
	protected OrderItem item;
	protected Date created;

	
	public BEAN build(BEAN bean){
		bean.setQuantity(quantity);
		bean.setReport(report);
		bean.setOrderItem(item);
		bean.setCreated(created);
		return bean;
	}
	
	public ReportItemBuilder<BEAN, BUILDER> setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public ReportItemBuilder<BEAN, BUILDER> setReport(Report report) {
		this.report = report;
		return this;
	}

	public ReportItemBuilder<BEAN, BUILDER> setItem(OrderItem item) {
		this.item = item;
		return this;
	}

	public ReportItemBuilder<BEAN, BUILDER> setDate(Date created) {
		this.created = created;
		return this;
	}

}
