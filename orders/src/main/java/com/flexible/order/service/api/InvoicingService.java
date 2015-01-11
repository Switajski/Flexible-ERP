package com.flexible.order.service.api;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.BeanUtil;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.report.Invoice;
import com.flexible.order.domain.report.InvoiceItem;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.itextpdf.builder.Unicode;
import com.flexible.order.reference.Currency;
import com.flexible.order.reference.ProductType;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.InvoicingAddressService;
import com.flexible.order.service.QuantityLeftCalculatorService;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.web.dto.ItemDto;

@Service
public class InvoicingService {

    @Autowired
    private ReportRepository reportRepo;
    @Autowired
    private ItemDtoConverterService itemDtoConverterService;
    @Autowired
    private InvoicingAddressService invoicingAddressService;
    @Autowired
    private QuantityLeftCalculatorService qtyLeftCalcService;

    @Transactional
    public Invoice invoice(InvoicingParameter invoicingParameter) {
        if (reportRepo.findByDocumentNumber(invoicingParameter.invoiceNumber) != null) throw new IllegalArgumentException("Rechnungsnr. existiert bereits");

        Map<ReportItem, Integer> risWithQty = itemDtoConverterService.mapItemDtosToReportItemsWithQty(invoicingParameter.shippingItemDtos);
        Invoice invoice = createInvoice(invoicingParameter);
        invoice.setInvoiceAddress(retrieveInvoicingAddress(risWithQty.keySet()));

        for (Entry<ReportItem, Integer> entry : risWithQty.entrySet()) {
            ReportItem shippingItem = entry.getKey();
            Integer qty = entry.getValue();

            qtyLeftCalcService.validateQuantity(qty, shippingItem);
            invoice.addItem(new InvoiceItem(
                    invoice,
                    shippingItem.getOrderItem(),
                    qty,
                    new Date()));

        }

        //TODO Currency Handling
        Amount shippingCosts = Amount.ZERO_EURO;
        for (ItemDto item : invoicingParameter.shippingItemDtos){
            if (item.productType == ProductType.SHIPPING)
                shippingCosts = shippingCosts.add(new Amount(item.priceNet, Currency.EUR));
        }
        invoice.setShippingCosts(shippingCosts);
        
        return reportRepo.save(invoice);
    }

    private Address retrieveInvoicingAddress(Set<ReportItem> reportItems) {
        Set<Address> ias = invoicingAddressService.retrieve(reportItems);
        if (ias.size() > 1) throw new IllegalArgumentException("Verschiedene Rechnungsadressen in Auftr" + Unicode.aUml + "gen gefunden: "
                + BeanUtil.createStringOfDifferingAttributes(ias));
        else if (ias.size() == 0) throw new IllegalStateException("Keine Rechnungsaddresse aus Kaufvertr" + Unicode.aUml + "gen gefunden");
        Address invoicingAddress = ias.iterator().next();
        return invoicingAddress;
    }

    private Invoice createInvoice(InvoicingParameter invoicingParameter) {
        Invoice invoice = new Invoice(invoicingParameter.invoiceNumber, invoicingParameter.paymentConditions, null);
        invoice.setBilling(invoicingParameter.billing);
        invoice.setCreated((invoicingParameter.created == null) ? new Date() : invoicingParameter.created);
        return invoice;
    }

}
