package com.flexible.order.web;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flexible.order.domain.Order;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.embeddable.ContactInformation;
import com.flexible.order.domain.embeddable.CustomerDetails;
import com.flexible.order.domain.report.CancelReport;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.Invoice;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Receipt;
import com.flexible.order.json.JsonObjectResponse;
import com.flexible.order.reference.Currency;
import com.flexible.order.service.api.AgreementService;
import com.flexible.order.service.api.DeliveryService;
import com.flexible.order.service.api.InvoicingParameter;
import com.flexible.order.service.api.InvoicingService;
import com.flexible.order.service.api.MarkPaidService;
import com.flexible.order.service.api.OrderService;
import com.flexible.order.service.process.parameter.BillingParameter;
import com.flexible.order.service.process.parameter.ConfirmParameter;
import com.flexible.order.service.process.parameter.DeliverParameter;
import com.flexible.order.service.process.parameter.OrderParameter;
import com.flexible.order.web.dto.JsonCreateReportRequest;
import com.flexible.order.web.helper.ExtJsResponseCreator;

@Controller
// TODO: distribute methods on business controllers
@RequestMapping("/transitions")
public class TransitionsController extends ExceptionController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoicingService invoicingService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private AgreementService agreementService;
    @Autowired
    private MarkPaidService markPaidService;

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse confirm(
            @RequestBody JsonCreateReportRequest confirmRequest)
            throws Exception {
        // TODO: see if Conversion factory / Jackson serializer has a strip to
        // null method
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setContact1(confirmRequest.contact1);
        contactInformation.setContact2(confirmRequest.contact2);
        contactInformation.setContact3(confirmRequest.contact3);
        contactInformation.setContact4(confirmRequest.contact4);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setContactInformation(contactInformation);
        customerDetails.setMark(confirmRequest.mark);
        customerDetails.setPaymentConditions(confirmRequest.paymentConditions);
        customerDetails.setSaleRepresentative(confirmRequest.saleRepresentative);
        customerDetails.setVatIdNo(confirmRequest.valueAddedTaxIdNo);
        customerDetails.setVendorNumber(confirmRequest.vendorNumber);

        ConfirmParameter confirmParameter = new ConfirmParameter(
                extractOrderNumber(confirmRequest),
                StringUtils.stripToNull(confirmRequest.orderConfirmationNumber),
                confirmRequest.expectedDelivery,
                confirmRequest.deliveryMethodNo,
                confirmRequest.createDeliveryAddress(),
                confirmRequest.createInvoiceAddress(),
                confirmRequest.items);
        confirmParameter.customerDetails = customerDetails;
        confirmParameter.customerNumber = confirmRequest.customerId;

        OrderConfirmation confirmationReport = orderService.confirm(
                confirmParameter);
        return ExtJsResponseCreator.createResponse(confirmationReport);
    }

    private String extractOrderNumber(JsonCreateReportRequest confirmRequest) {
        if (confirmRequest.items.isEmpty()) throw new IllegalArgumentException(
                "Liste der Auftragspositionen ist leer");
        return confirmRequest.items.iterator().next().orderNumber;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse order(@RequestBody JsonCreateReportRequest deliverRequest)
            throws Exception {
        deliverRequest.validate();

        OrderParameter orderParameter = new OrderParameter(
                deliverRequest.customerId,
                deliverRequest.orderNumber,
                deliverRequest.created,
                deliverRequest.items);
        orderParameter.expectedDelivery = deliverRequest.expectedDelivery;

        Order order = orderService.order(
                orderParameter);

        return ExtJsResponseCreator.createResponse(order);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse invoice(
            @RequestBody JsonCreateReportRequest invoicingRequest)
            throws Exception {
        invoicingRequest.validate();

        InvoicingParameter invoicingParameter = new InvoicingParameter(
                invoicingRequest.invoiceNumber,
                invoicingRequest.paymentConditions,
                invoicingRequest.created,
                invoicingRequest.items,
                invoicingRequest.billing);
        invoicingParameter.customerNumber = invoicingRequest.customerId;
        Invoice invoice = invoicingService.invoice(
                invoicingParameter);
        return ExtJsResponseCreator.createResponse(invoice);
    }

    @RequestMapping(value = "/deliver", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse deliver(
            @RequestBody JsonCreateReportRequest deliverRequest)
            throws Exception {
        deliverRequest.validate();

        DeliverParameter deliverParameter = new DeliverParameter(
                deliverRequest.deliveryNotesNumber,
                deliverRequest.trackNumber,
                deliverRequest.packageNumber,
                new Amount(deliverRequest.shipment, Currency.EUR),
                deliverRequest.created,
                deliverRequest.items);
        deliverParameter.customerNumber = deliverRequest.customerId;
        DeliveryNotes dn = deliveryService.deliver(
                deliverParameter);
        return ExtJsResponseCreator.createResponse(dn);
    }

    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse agree(
            @RequestParam(value = "orderAgreementNumber", required = true) String orderAgreementNumber,
            @RequestParam(value = "orderConfirmationNumber", required = true) String orderConfirmationNumber) {
        OrderConfirmation orderAgreement = agreementService.agree(orderConfirmationNumber, orderAgreementNumber);
        return ExtJsResponseCreator.createResponse(orderAgreement);
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse deleteOrder(
            @RequestParam(value = "orderNumber", required = true) String orderNumber) {
        orderService.deleteOrder(orderNumber);
        return ExtJsResponseCreator.createResponse(orderNumber);
    }

    @RequestMapping(value = "/deleteReport", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse deleteReport(
            @RequestParam(value = "documentNumber", required = true) String documentNumber)
            throws Exception {
        orderService.deleteReport(documentNumber);
        return ExtJsResponseCreator.createResponse(documentNumber);
    }

    @RequestMapping(value = "/cancelReport", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse cancelConfirmationReport(
            @RequestParam(value = "confirmationNumber", required = true) String orderConfirmationNumber)
            throws Exception {
        CancelReport cr = orderService
                .cancelReport(orderConfirmationNumber);
        return ExtJsResponseCreator.createResponse(cr);
    }

    @RequestMapping(value = "/markPaid", method = RequestMethod.POST)
    public @ResponseBody
    JsonObjectResponse markPaid(
            @RequestParam(value = "invoiceNumber", required = true) String invoiceNumber)
            throws Exception {
        Receipt receipt = markPaidService.markAsPayed(new BillingParameter(invoiceNumber, "B"
                + invoiceNumber, new Date()));
        return ExtJsResponseCreator.createResponse(receipt);
    }

}
