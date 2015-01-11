package com.flexible.order.repository.specification;

import com.flexible.order.domain.report.InvoiceItem;
import com.flexible.order.domain.report.ReceiptItem;

public class IssuedItemSpec extends AbstractOpenReportItemSpecification {

    public IssuedItemSpec() {
        super(InvoiceItem.class, ReceiptItem.class);
    }
    
}
