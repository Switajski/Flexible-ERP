package com.flexible.order.testhelper.EntityBuilder;

import com.flexible.order.domain.report.InvoiceItem;

public class InvoiceItemBuilder extends ReportItemBuilder<InvoiceItem, Builder<InvoiceItem>> {

	@Override
	public InvoiceItem build() {
		InvoiceItem ii = new InvoiceItem();
		super.build(ii);
		return ii;
	}
}
