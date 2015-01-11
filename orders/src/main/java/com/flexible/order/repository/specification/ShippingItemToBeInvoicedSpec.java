package com.flexible.order.repository.specification;

import com.flexible.order.domain.report.InvoiceItem;
import com.flexible.order.domain.report.ShippingItem;

public class ShippingItemToBeInvoicedSpec extends AbstractOpenReportItemSpecification {

    public ShippingItemToBeInvoicedSpec() {
        super(ShippingItem.class, InvoiceItem.class);
    }
    
}
