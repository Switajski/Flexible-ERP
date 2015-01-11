package com.flexible.order.service.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.embeddable.DeliveryMethod;
import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.exceptions.ContradictoryPurchaseAgreementException;
import com.flexible.order.reference.Currency;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.QuantityLeftCalculatorService;
import com.flexible.order.service.ShippingAddressService;
import com.flexible.order.service.api.DeliveryService;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.service.process.parameter.DeliverParameter;
import com.flexible.order.testhelper.EntityBuilder.AddressBuilder;
import com.flexible.order.testhelper.EntityBuilder.ConfirmationItemBuilder;
import com.flexible.order.testhelper.EntityBuilder.DeliveryMethodBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderConfirmationBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderItemBuilder;
import com.flexible.order.testhelper.EntityBuilder.ProductBuilder;
import com.flexible.order.web.dto.ItemDto;

public class DeliveryServiceTest {

    private static final Address ADDRESS_1 = AddressBuilder.buildWithGeneratedAttributes(2);

    private static final Address ADDRESS_2 = AddressBuilder.buildWithGeneratedAttributes(45);

    private static final String DN_NO = "L123";
    
    private static final Amount SHIPPING_COSTS = new Amount(BigDecimal.TEN, Currency.EUR);

    private static final DeliveryMethod DELIVERY_METHOD = new DeliveryMethodBuilder().dhl().build();

    @InjectMocks
    DeliveryService deliveryService = new DeliveryService();
    @Mock
    ItemDtoConverterService convService;
    @Mock
    ReportRepository reportRepo;
    @Mock
    ShippingAddressService shippingAddressService;
    @Mock
    QuantityLeftCalculatorService qtyLeftCalcService;

    @Test(expected = ContradictoryPurchaseAgreementException.class)
    public void shouldRejectDeliveryIfContradictoryShippingAdressesExist() {
        // GIVEN
        mockValidation();
        givenReportItems();
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class)))
                .thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1, ADDRESS_2)));

        // WHEN / THEN
        deliveryService.deliver(givenDeliverParameter());

    }
    
    private void givenReportItems() {
        when(convService.mapItemDtosToReportItemsWithQty(Matchers.anyCollectionOf(ItemDto.class)))
                .thenReturn(givenItemMap());
    }

    private DeliverParameter givenDeliverParameter() {
        DeliverParameter deliverParam = new DeliverParameter();
        deliverParam.deliveryNotesNumber = DN_NO;
        return deliverParam;
    }

    @Test
    public void shouldDeliverIfContradictoryExpectedDeliveryDatesExistAndIgnoreFlagIsSet() {
        mockValidation();
        givenReportItems();
        DeliverParameter deliverParameter = givenDeliverParameter();
        deliverParameter.ignoreContradictoryExpectedDeliveryDates = true;
        givenOneShippingAddress();

        // WHEN
        deliveryService.deliver(deliverParameter);

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }

    @Test
    public void shouldDeliverIfNoExpectedDeliveryDateIsSet() {
        mockValidation();
        givenReportItems();
        givenOneShippingAddress();

        // WHEN
        deliveryService.deliver(givenDeliverParameter());

        // THEN
        assertThatDeliveryNotesIsSavedWithTwoItems();
    }
    
    @Test
    public void shouldCreateShippingCosts(){
        // GIVEN
        mockValidation();
        givenReportItems();
        givenOneShippingAddress();
        DeliverParameter param = givenDeliverParameter();
        param.shipment = SHIPPING_COSTS;
        param.deliveryMethod = DELIVERY_METHOD;
        
        // WHEN
        deliveryService.deliver(param);
        
        // THEN
        ArgumentCaptor<DeliveryNotes> deliveryNotes = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(deliveryNotes.capture());
        assertThat(deliveryNotes.getValue().getShippingCosts(), is(equalTo(SHIPPING_COSTS)));
        assertThat(deliveryNotes.getValue().getDeliveryMethod(), is(equalTo(DELIVERY_METHOD)));
        
    }

    private void givenOneShippingAddress() {
        when(shippingAddressService.retrieve(Matchers.anySetOf(ReportItem.class))).thenReturn(new HashSet<Address>(Arrays.asList(ADDRESS_1)));
    }

    private void assertThatDeliveryNotesIsSavedWithTwoItems() {
        ArgumentCaptor<DeliveryNotes> argument = ArgumentCaptor.forClass(DeliveryNotes.class);
        verify(reportRepo).save(argument.capture());
        assertThat(argument.getValue().getItems().size(), is(equalTo(2)));
    }

    private void mockValidation() {
        MockitoAnnotations.initMocks(this);
        when(reportRepo.findByDocumentNumber(DN_NO)).thenReturn(null);
    }

    private Map<ReportItem, Integer> givenItemMap() {
        Map<ReportItem, Integer> map = new HashMap<ReportItem, Integer>();
        map.put(givenItemWith(null), 2);
        map.put(givenItemOtherWith(null), 5);
        return map;
    }

    private ReportItem givenItemWith(PurchaseAgreement purchaseAgreement) {
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

    private ReportItem givenItemOtherWith(PurchaseAgreement pa) {
        return new ConfirmationItemBuilder()
                .setItem(
                        new OrderItemBuilder()
                                .setProduct(new ProductBuilder().build())
                                .setOrderedQuantity(25)
                                .build())
                .setQuantity(9)
                .setReport(
                        new OrderConfirmationBuilder()
                                .setAgreementDetails(pa)
                                .build())
                .build();
    }

}
