package de.switajski.priebes.flexibleorders.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.specification.CompletedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ConfirmedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.OrderedSpecification;
import de.switajski.priebes.flexibleorders.domain.specification.ShippedSpecification;
import de.switajski.priebes.flexibleorders.web.entities.ReportItem;

@Entity
@JsonAutoDetect
public class OrderItem extends GenericEntity implements Comparable<OrderItem> {

	@JsonIgnore
	@NotNull
	@OneToMany(mappedBy = "deliveryHistory", fetch = FetchType.LAZY)
	private Set<HandlingEvent> deliveryHistory = new HashSet<HandlingEvent>();
	
	@NotNull
	private Integer orderedQuantity;
	
	private Amount negotiatedPriceNet;
	
	@NotNull
	@Embedded
	private Product product;
	
	private String packageNumber;
	
	private String trackingNumber;
	
	@JsonIgnore
	@NotNull
	@ManyToOne
	private FlexibleOrder flexibleOrder;

	protected OrderItem() {}
	
	/**
	 * Constructor with all attributes needed to create a valid item
	 * @param order
	 * @param product
	 * @param orderedQuantity
	 */
	public OrderItem(FlexibleOrder order, Product product, int orderedQuantity){
		this.deliveryHistory = new HashSet<HandlingEvent>();
		this.flexibleOrder = order;
		this.orderedQuantity = orderedQuantity;
		setProduct(product);
		
		// handle birectional relationship
		if (!order.getItems().contains(this))
			order.getItems().add(this);
	}
	
	public String toString(){
		String s = "#"+getId().toString() + ": " + getOrderedQuantity() + 
				" x " + getProduct().getName() + " " + provideStatus();
		return s;
	}
	
	@Override
	public int compareTo(OrderItem o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Set<HandlingEvent> getDeliveryHistory() {
		return deliveryHistory;
	}

	public void setDeliveryHistory(Set<HandlingEvent> deliveryHistory) {
		this.deliveryHistory = deliveryHistory;
	}

	public Amount getNegotiatedPriceNet() {
		return negotiatedPriceNet;
	}

	public void setNegotiatedPriceNet(Amount negotiatedPriceNet) {
		this.negotiatedPriceNet = negotiatedPriceNet;
	}

	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public FlexibleOrder getOrder() {
		return flexibleOrder;
	}

	public void setOrder(FlexibleOrder order) {
		if (!order.getItems().contains(this))
			order.getItems().add(this);
		this.flexibleOrder = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(String packageNumber) {
		this.packageNumber = packageNumber;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public Set<HandlingEvent> getAllHesOfType(HandlingEventType type) {
		Set<HandlingEvent> hesOfType = new HashSet<HandlingEvent>();
		for (HandlingEvent he: deliveryHistory){
			if (he.getType() == type)
				hesOfType.add(he);
		}
		return hesOfType;
	}

	/**
	 * handles bidirectional relationship
	 * @param handlingEvent has no other Report than this. If null, this orderItem will be set.
	 */
	public void addHandlingEvent(HandlingEvent handlingEvent) {
		//prevent endless loop
		if (deliveryHistory.contains(handlingEvent)) return;
		deliveryHistory.add(handlingEvent);
		handlingEvent.setOrderItem(this);
	}
	
	public void removeHandlingEvent(HandlingEvent handlingEvent){
		if (!deliveryHistory.contains(handlingEvent)) return;
		deliveryHistory.remove(handlingEvent);
		handlingEvent.setOrderItem(null);
	}

	public Report getReport(String invoiceNo) {
		for (HandlingEvent he: getDeliveryHistory())
			if (he.getReport().getDocumentNumber().equals(invoiceNo))
				return he.getReport();
		return null;
	}
	
	/**
	 * returns the summed quantity of all HandlingEvents of given HandlingEventType
	 * 
	 * @return null if no HandlingEvents of given type found
	 */
	public Integer getHandledQuantity(HandlingEventType type) {
		Set<HandlingEvent> hes = getAllHesOfType(type);
		if (hes.isEmpty()) return 0;
		int summed = 0;
		for (HandlingEvent he: hes){
			summed =+ he.getQuantity();
		}
		return summed;
	}

	public Integer calculateQuantityLeft() {
		return 666;
	}

	public String provideStatus() {
		String s = "";
		if (new CompletedSpecification().isSatisfiedBy(this)) s+= "fertig";
		else if (new ShippedSpecification(false, false).isSatisfiedBy(this)) s += "versendet";
		else if (new ConfirmedSpecification(false, false).isSatisfiedBy(this)) s+= "best�tigt";
		else if (new OrderedSpecification().isSatisfiedBy(this)) s+= "bestellt";
		return s;
	}
	
	public ReportItem toReportItem(){
		ReportItem item = new ReportItem();
		item.setId(getId());
		item.setQuantity(getOrderedQuantity());
		item.setQuantityLeft(getOrderedQuantity() - getHandledQuantity(HandlingEventType.CONFIRM));
		item.setCustomer(getOrder().getCustomer().getId());
		item.setId(getId());
		item.setOrderNumber(getOrder().getOrderNumber());
		if (getNegotiatedPriceNet() != null)
			item.setPriceNet(getNegotiatedPriceNet().getValue());
		item.setProduct(getProduct().getProductNumber());
		item.setProductName(getProduct().getName());
		item.setStatus(provideStatus());
		item.setCustomerName(getOrder().getCustomer().getShortName());
		item.setCreated(getCreated());
		return item;
	}
	
}
