package com.flexible.order.repository.specification;

import com.flexible.order.domain.report.InvoiceItem;
import com.flexible.order.domain.report.ReceiptItem;

public class InvoiceItemToBePaidSpec extends AbstractOpenReportItemSpecification {

    public InvoiceItemToBePaidSpec() {
        super(InvoiceItem.class, ReceiptItem.class);
    }
    
}
