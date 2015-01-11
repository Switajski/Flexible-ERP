package com.flexible.order.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.exceptions.NotFoundException;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.repository.specification.AgreedItemsToBeShippedSpec;
import com.flexible.order.repository.specification.ConfirmationItemToBeAgreedSpec;
import com.flexible.order.repository.specification.InvoiceItemToBePaidSpec;
import com.flexible.order.repository.specification.IssuedItemSpec;
import com.flexible.order.repository.specification.ReceiptItemCompletedSpec;
import com.flexible.order.repository.specification.ShippingItemToBeInvoicedSpec;
import com.flexible.order.web.helper.ProductionState;

@Service
public class StatusFilterDispatcher {

    @Autowired
    private CustomerRepository customerRepo;

    public Specification<ReportItem> dispatchStatus(ProductionState processStep) {
        if (processStep == null) throw new IllegalArgumentException("Status nicht angegeben");
        
        switch (processStep) {
            case CONFIRMED:
                return new ConfirmationItemToBeAgreedSpec();

            case AGREED:
                return new AgreedItemsToBeShippedSpec();

            case SHIPPED:
                return new ShippingItemToBeInvoicedSpec();

            case INVOICED:
                return new InvoiceItemToBePaidSpec();

            case COMPLETED:
                return new ReceiptItemCompletedSpec();

            case ISSUED:
                return new IssuedItemSpec();
            
            case DELIVERED:
                return new ShippingItemToBeInvoicedSpec(); 

        }
        throw new NotFoundException("Status "+ processStep +"nicht gefunden");

    }

}
