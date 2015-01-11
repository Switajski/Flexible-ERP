package com.flexible.order.testhelper.EntityBuilder;

import com.flexible.order.domain.report.ReceiptItem;

public class ReceiptItemBuilder extends ReportItemBuilder<ReceiptItem, Builder<ReceiptItem>>
		implements Builder<ReceiptItem> {

	public ReceiptItem build() {
		ReceiptItem ii = new ReceiptItem();
		super.build(ii);
		return ii;
	}

}
