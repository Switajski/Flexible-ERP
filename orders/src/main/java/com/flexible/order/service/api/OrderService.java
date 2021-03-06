package com.flexible.order.service.api;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.CatalogProduct;
import com.flexible.order.domain.Customer;
import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.Product;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.CancelReport;
import com.flexible.order.domain.report.CancellationItem;
import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.itextpdf.builder.Unicode;
import com.flexible.order.reference.Currency;
import com.flexible.order.reference.OriginSystem;
import com.flexible.order.reference.ProductType;
import com.flexible.order.repository.CatalogDeliveryMethodRepository;
import com.flexible.order.repository.CatalogProductRepository;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.repository.OrderItemRepository;
import com.flexible.order.repository.OrderRepository;
import com.flexible.order.repository.ReportItemRepository;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.CatalogProductServiceImpl;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.service.process.parameter.ConfirmParameter;
import com.flexible.order.service.process.parameter.OrderParameter;
import com.flexible.order.web.ExceptionController;
import com.flexible.order.web.dto.ItemDto;

/**
 * TODO: Add validation to service layer:</br> see <a href=
 * "http://docs.spring.io/spring/docs/3.0.0.RC3/reference/html/ch05s07.html">
 * http://docs.spring.io/spring/docs/3.0.0.RC3/reference/html/ch05s07.html</a>
 * </br> and converters from request object to ItemDto
 * 
 * @author Marek Switajski
 * 
 */
@Service
public class OrderService {
    
    private static Logger log = Logger.getLogger(OrderService.class);

	@Autowired
	private CatalogDeliveryMethodRepository deliveryMethodRepo;
	@Autowired
	private ReportRepository reportRepo;
	@Autowired
	private OrderItemRepository orderItemRepo;
	@Autowired
	private ReportItemRepository reportItemRepo;
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private ItemDtoConverterService itemDtoConverterService;
	@Autowired
    private CatalogProductServiceImpl cProductService;

	/**
	 * Creates initially an order with its order items
	 * @param orderParameter
	 * 
	 * @return created order, when successfully persisted
	 */
	@Transactional
	public Order order(OrderParameter orderParameter) {
		if (orderParameter.customerNumber == null || orderParameter.orderNumber == null || orderParameter.reportItems.isEmpty())
			throw new IllegalArgumentException("Parameter sind nicht vollst"+Unicode.aUml+"ndig");
		if (orderRepo.findByOrderNumber(orderParameter.orderNumber) != null)
			throw new IllegalArgumentException("Bestellnr existiert bereits");
		// TODO: Customer entity has nothing to do here!
		Customer customer = customerRepo.findByCustomerNumber(orderParameter.customerNumber);
		if (customer == null)
			throw new IllegalArgumentException(
					"Keinen Kunden mit gegebener Kundennr. gefunden");

		Order order = new Order(
				customer,
				OriginSystem.FLEXIBLE_ORDERS,
				orderParameter.orderNumber);
		order.setCreated((orderParameter.created == null) ? new Date() : orderParameter.created);
		PurchaseAgreement purchaseAgreement = new PurchaseAgreement();
		purchaseAgreement.setCustomerNumber(customer.getCustomerNumber());
		purchaseAgreement.setExpectedDelivery(orderParameter.expectedDelivery);
		
		for (ItemDto ri : orderParameter.reportItems) {
		    validate(ri);
			Product product = (ri.product.equals(0L)) ? createCustomProduct(ri) : createProductFromCatalog(ri);
			OrderItem oi = new OrderItem(
					order,
					product,
					ri.quantity);
			oi.setNegotiatedPriceNet(new Amount(
					ri.priceNet,
					Currency.EUR));
			order.addOrderItem(oi);
		}

		return orderRepo.save(order);
	}
	
	private void validate(ItemDto ri) {
	    if (ri.productName == null)
	        throw new IllegalArgumentException("Produktnamen nicht angegeben");
    }

    @Transactional
	public OrderConfirmation confirm(ConfirmParameter confirmParameter) {
		Address invoiceAddress = confirmParameter.invoiceAddress;
				Address shippingAddress = confirmParameter.shippingAddress;
		validateConfirm(confirmParameter.orderNumber, confirmParameter.confirmNumber, confirmParameter.orderItems, shippingAddress);

		Order order = orderRepo.findByOrderNumber(confirmParameter.orderNumber);
		if (order == null)
			throw new IllegalArgumentException("Bestellnr. nicht gefunden");

		Customer cust = order.getCustomer();
		Address address = (cust.getInvoiceAddress() == null) ? cust.getShippingAddress() : cust.getInvoiceAddress();
		shippingAddress = (shippingAddress.isComplete()) ? shippingAddress : address;
		invoiceAddress = (invoiceAddress.isComplete()) ? invoiceAddress : address;
		
		PurchaseAgreement pAgree = new PurchaseAgreement();
		pAgree.setShippingAddress(shippingAddress);
		pAgree.setInvoiceAddress(invoiceAddress);
		pAgree.setExpectedDelivery(confirmParameter.expectedDelivery);
		pAgree.setCustomerNumber(confirmParameter.customerNumber);
		if (confirmParameter.deliveryMethodNo != null)
		    pAgree.setDeliveryMethod(deliveryMethodRepo.findOne(confirmParameter.deliveryMethodNo).getDeliveryMethod());

		OrderConfirmation cr = new OrderConfirmation();
		cr.setDocumentNumber(confirmParameter.confirmNumber);
		cr.setPurchaseAgreement(pAgree);
		cr.setCustomerDetails(confirmParameter.customerDetails);

		for (ItemDto entry : confirmParameter.orderItems) {
			OrderItem oi = orderItemRepo.findOne(entry.id);
			if (oi == null)
				throw new IllegalArgumentException(
						"Bestellposition nicht gefunden");
			cr.addItem(new ConfirmationItem(cr, oi,
					entry.quantityLeft, new Date()));
		}
		return reportRepo.save(cr);
	}
	
	private void validateConfirm(String orderNumber, String confirmNumber,
			List<ItemDto> orderItems, Address shippingAddress) {
		if (reportRepo.findByDocumentNumber(confirmNumber) != null)
			throw new IllegalArgumentException("Auftragsnr. " + confirmNumber
					+ " besteht bereits");
		if (orderItems.isEmpty())
			throw new IllegalArgumentException("Keine Positionen angegeben");
		if (orderNumber == null)
			throw new IllegalArgumentException("Keine Bestellnr angegeben");
		if (confirmNumber == null)
			throw new IllegalArgumentException("Keine AB-nr angegeben");
		if (shippingAddress == null)
			throw new IllegalArgumentException("Keine Lieferadresse angegeben");
		for (ItemDto item : orderItems) {
			if (item.id == null)
				throw new IllegalArgumentException("Position hat keine Id");
		}
	}


	private Product createProductFromCatalog(ItemDto ri) {
		Product product;
		
		try {
            CatalogProduct cProduct = cProductService.findByProductNumber(ri.product);
            if (cProduct == null)
            	throw new IllegalArgumentException("Artikelnr nicht gefunden");
            product = cProduct.toProduct();
            return product;
        }
        catch (Exception e) {
            log.warn("Could not find ProductNumber " + ri.product + " in Catalog");
            
            Product p = new Product();
            p.setName((ri.productName));
            p.setProductType(ProductType.PRODUCT);
            p.setProductNumber(ri.product);
            return p;
        }
	}

	private Product createCustomProduct(ItemDto ri) {
		Product p = new Product();
		p.setName(ri.productName);
		p.setProductType(ProductType.CUSTOM);
		p.setProductNumber("0");
		return p;
	}

	@Transactional
	private OrderItem createShippingCosts(Amount shipment, Order order) {
		Product product = new Product();
		product.setProductType(ProductType.SHIPPING);
		product.setName("Versand");

		OrderItem shipOi = new OrderItem(order, product, 1);
		shipOi.setNegotiatedPriceNet(shipment);

		return orderItemRepo.save(shipOi);
	}

	@Transactional
	public boolean deleteOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		if (order == null)
			throw new IllegalArgumentException(
					"Bestellnr. zum l"+Unicode.oUml+"schen nicht gefunden"); 
		orderRepo.delete(order);
		return true;
	}

	@Transactional
	public CancelReport cancelReport(String reportNo) {
		Report cr = reportRepo.findByDocumentNumber(reportNo);
		if (cr == null)
			throw new IllegalArgumentException("Angegebene Dokumentennummer nicht gefunden");
		CancelReport cancelReport = createCancelReport(cr);
		return reportRepo.save(cancelReport);
	}
	
	private CancelReport createCancelReport(Report cr) {
		CancelReport cancelReport = new CancelReport("ABGEBROCHEN-"
				+ cr.getDocumentNumber());
		for (ReportItem he : cr.getItems()) {
			cancelReport.addItem(new CancellationItem(
					cancelReport,
					he.getOrderItem(),
					he.getQuantity(),
					new Date()));
		}
		return cancelReport;
	}

	@Transactional
	public boolean deleteReport(String reportNumber) {
		Report r = reportRepo.findByDocumentNumber(reportNumber);
		if (r == null)
			throw new IllegalArgumentException(
					"Bericht zum l"+Unicode.oUml+"schen nicht gefunden");
		reportRepo.delete(r);
		return true;
	}

	@Transactional(readOnly = true)
	public Order retrieveOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		order.getCustomer();
		order.getItems();
		return orderRepo.findByOrderNumber(orderNumber);
	}

}
