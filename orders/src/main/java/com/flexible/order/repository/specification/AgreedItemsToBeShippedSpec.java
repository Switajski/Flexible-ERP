package com.flexible.order.repository.specification;

import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.ShippingItem;

public class AgreedItemsToBeShippedSpec extends AbstractOpenReportItemSpecification{

    public AgreedItemsToBeShippedSpec() {
        super(ConfirmationItem.class, ShippingItem.class);
    }
    
}
