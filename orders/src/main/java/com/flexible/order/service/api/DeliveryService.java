package com.flexible.order.service.api;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.BeanUtil;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.domain.report.ShippingItem;
import com.flexible.order.exceptions.ContradictoryPurchaseAgreementException;
import com.flexible.order.exceptions.NotFoundException;
import com.flexible.order.itextpdf.builder.Unicode;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.QuantityLeftCalculatorService;
import com.flexible.order.service.ShippingAddressService;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.service.process.parameter.DeliverParameter;

@Service
public class DeliveryService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoConverterService convService;
    @Autowired
    private ShippingAddressService shippingAddressService;
    @Autowired
    private QuantityLeftCalculatorService qtyLeftCalcService;

    @Transactional
    public DeliveryNotes deliver(DeliverParameter deliverParameter) {
        if (reportRepo.findByDocumentNumber(deliverParameter.deliveryNotesNumber) != null) throw new IllegalArgumentException(
                "Lieferscheinnummer existiert bereits");

        Map<ReportItem, Integer> risWithQty = convService.mapItemDtosToReportItemsWithQty(deliverParameter.agreementItemDtos);
        Set<ReportItem> ris = risWithQty.keySet();

        Address shippingAddress = retrieveInvoicingAddress(ris, deliverParameter.ignoreContradictoryExpectedDeliveryDates);
        DeliveryNotes deliveryNotes = createDeliveryNotes(deliverParameter);
        deliveryNotes.setShippingCosts(deliverParameter.shipment);
        deliveryNotes.setDeliveryMethod(deliverParameter.deliveryMethod);

        for (Entry<ReportItem, Integer> riWithQty : risWithQty.entrySet()) {
            ReportItem agreementItem = riWithQty.getKey();
            int qty = riWithQty.getValue();
            OrderItem orderItemToBeDelivered = agreementItem.getOrderItem();

            qtyLeftCalcService.validateQuantity(qty, agreementItem);

            deliveryNotes.addItem(new ShippingItem(
                    deliveryNotes,
                    orderItemToBeDelivered,
                    qty,
                    new Date()));

            deliveryNotes.setShippedAddress(shippingAddress);
        }

        return reportRepo.save(deliveryNotes);
    }

    private DeliveryNotes createDeliveryNotes(DeliverParameter deliverParameter) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setDocumentNumber(deliverParameter.deliveryNotesNumber);
        deliveryNotes.setCreated(deliverParameter.created == null ? new Date() : deliverParameter.created);
        return deliveryNotes;
    }

    private Address retrieveInvoicingAddress(Set<ReportItem> reportItems, boolean ignoreContradictoryExpectedDeliveryDates) {
        Set<Address> ias = shippingAddressService.retrieve(reportItems);
        if (ias.size() > 1 && !ignoreContradictoryExpectedDeliveryDates) throw new ContradictoryPurchaseAgreementException(
                "Verschiedene Lieferadressen in Auftr" + Unicode.aUml + "gen gefunden: "
                        + BeanUtil.createStringOfDifferingAttributes(ias));
        else if (ias.size() == 0) throw new NotFoundException("Keine Lieferaddresse aus Kaufvertr" + Unicode.aUml + "gen gefunden");
        Address invoicingAddress = ias.iterator().next();
        return invoicingAddress;
    }

}
