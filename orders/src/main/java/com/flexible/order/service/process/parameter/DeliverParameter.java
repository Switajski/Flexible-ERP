package com.flexible.order.service.process.parameter;

import java.util.Date;
import java.util.List;

import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.embeddable.DeliveryMethod;
import com.flexible.order.web.dto.ItemDto;

public class DeliverParameter {
	public String deliveryNotesNumber;
	public Long customerNumber;
	public String trackNumber;
	public String packageNumber;
	//TODO: make shipment an itemDto, in which you can choose the deliveryMethod
	public Amount shipment;
	public Date created;
	public List<ItemDto> agreementItemDtos;
    public boolean ignoreContradictoryExpectedDeliveryDates;
    public DeliveryMethod deliveryMethod;

	public DeliverParameter(String deliveryNotesNumber, String trackNumber,
			String packageNumber, Amount shipment, Date created,
			List<ItemDto> agreementItemDtos) {
		this.deliveryNotesNumber = deliveryNotesNumber;
		this.trackNumber = trackNumber;
		this.shipment = shipment;
		this.packageNumber = packageNumber;
		this.created = created;
		this.agreementItemDtos = agreementItemDtos;
	}

    public DeliverParameter() {
    }
}