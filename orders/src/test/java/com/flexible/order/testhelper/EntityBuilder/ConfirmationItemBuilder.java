package com.flexible.order.testhelper.EntityBuilder;

import com.flexible.order.domain.report.ConfirmationItem;

public class ConfirmationItemBuilder extends ReportItemBuilder<ConfirmationItem, Builder<ConfirmationItem>>{

	@Override
	public ConfirmationItem build() {
		ConfirmationItem ii = new ConfirmationItem();
		super.build(ii);
		return ii;
	}
}
