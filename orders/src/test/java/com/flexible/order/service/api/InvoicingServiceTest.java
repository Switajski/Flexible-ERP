package com.flexible.order.service.api;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.InvoicingAddressService;
import com.flexible.order.service.api.InvoicingParameter;
import com.flexible.order.service.api.InvoicingService;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.testhelper.EntityBuilder.AddressBuilder;
import com.flexible.order.testhelper.EntityBuilder.ConfirmationItemBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderConfirmationBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderItemBuilder;
import com.flexible.order.testhelper.EntityBuilder.ProductBuilder;
import com.flexible.order.web.dto.ItemDto;

public class InvoicingServiceTest {

    @InjectMocks
    InvoicingService invoicingService = new InvoicingService();
    @Mock
    ReportRepository reportRepo;
    @Mock
    ItemDtoConverterService itemDtoConverterService;
    @Mock
    InvoicingAddressService invoicingAddressService;

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvoicingIfDifferentInvoicingAddressesExist() {
        // GIVEN
        MockitoAnnotations.initMocks(this);
        when(reportRepo.findByDocumentNumber(Matchers.anyString())).thenReturn(null);
        when(itemDtoConverterService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class))).thenReturn(givenShippingItemMap());
        when(invoicingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(givenInvoicingAddresses());
        InvoicingParameter invoicingParameter = new InvoicingParameter();
        invoicingParameter.invoiceNumber = "L123";

        // WHEN / THEN
        invoicingService.invoice(invoicingParameter);

    }

    private Set<Address> givenInvoicingAddresses() {
        Set<Address> as = new HashSet<Address>();
        as.add(AddressBuilder.buildWithGeneratedAttributes(1));
        as.add(AddressBuilder.buildWithGeneratedAttributes(2));
        return as;
    }

    private Map<ReportItem, Integer> givenShippingItemMap() {
        Map<ReportItem, Integer> map = new HashMap<ReportItem, Integer>();
        map.put(givenAgreementItemWith(null), 2);
        return map;
    }

    private ReportItem givenAgreementItemWith(PurchaseAgreement purchaseAgreement) {
        return new ConfirmationItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(12)
                                .build())
                .setQuantity(6)
                .setReport(
                        new OrderConfirmationBuilder()
                                .setAgreementDetails(purchaseAgreement)
                                .build())
                .build();
    }

}
