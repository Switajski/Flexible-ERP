package com.flexible.order.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.reference.Currency;
import com.flexible.order.repository.CatalogProductRepository;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.repository.OrderItemRepository;
import com.flexible.order.repository.OrderRepository;
import com.flexible.order.repository.ReportItemRepository;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.CatalogProductServiceImpl;
import com.flexible.order.service.api.OrderService;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.service.process.parameter.OrderParameter;
import com.flexible.order.testhelper.EntityBuilder.CatalogProductBuilder;
import com.flexible.order.testhelper.EntityBuilder.CustomerBuilder;
import com.flexible.order.web.dto.ItemDto;

public class OrderServiceTest {

    private static final Integer ORDERED_QUANTITY = 3;
    private static final String PRODUCT_NO = "3";
    private static final Long CUSTOMER_ID = 2l;
    private static final BigDecimal PRICE_NET = new BigDecimal(5.55);

    @Mock
    private ReportRepository reportRepoMock;
    @Mock
    private OrderItemRepository orderItemRepoMock;
    @Mock
    private CatalogProductRepository cProductRepoMock;
    @Mock
    private ReportItemRepository heRepoMock;
    @Mock
    private CustomerRepository customerRepoMock;
    @Mock
    private OrderRepository orderRepoMock;
    @Mock
    private ItemDtoConverterService itemDtoConverterService;
    @Mock
    private CatalogProductServiceImpl catalogProductService;
    @InjectMocks
    private OrderService orderService = new OrderService();

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(customerRepoMock.findByCustomerNumber(CUSTOMER_ID))
                .thenReturn(CustomerBuilder.buildWithGeneratedAttributes(2));
        Mockito.when(catalogProductService.findByProductNumber(PRODUCT_NO))
                .thenReturn(CatalogProductBuilder.buildWithGeneratedAttributes(Integer.valueOf(PRODUCT_NO)));

    }

    @Test
    public void shouldOrder() {
        orderService.order(new OrderParameter(CUSTOMER_ID, "123", new Date(), givenReportItems()));

        ArgumentCaptor<Order> orderVarToBeSaved = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepoMock).save(orderVarToBeSaved.capture());

        assertSavedAsExpected(orderVarToBeSaved.getValue());
    }

    private void assertSavedAsExpected(Order orderToBeSaved) {
        assertNotNull(orderToBeSaved.getItems());

        OrderItem orderItem = orderToBeSaved.getItems().iterator().next();
        assertEquals(ORDERED_QUANTITY, orderItem.getOrderedQuantity());
        assert (orderItem.getReportItems().isEmpty());
        assertEquals(new Amount((PRICE_NET), Currency.EUR), orderItem.getNegotiatedPriceNet());
    }

    private List<ItemDto> givenReportItems() {
        List<ItemDto> reportItems = new ArrayList<ItemDto>();
        ItemDto itemDto = new ItemDto();
        itemDto.customer = CUSTOMER_ID;
        itemDto.product = PRODUCT_NO;
        itemDto.priceNet = PRICE_NET;
        itemDto.productName = "productName";
        itemDto.quantity = ORDERED_QUANTITY;
        reportItems.add(itemDto);
        return reportItems;
    }

}
