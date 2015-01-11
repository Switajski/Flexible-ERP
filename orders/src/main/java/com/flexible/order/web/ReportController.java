package com.flexible.order.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.flexible.order.domain.Customer;
import com.flexible.order.domain.Order;
import com.flexible.order.domain.report.CreditNote;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.Invoice;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Report;
import com.flexible.order.json.JsonObjectResponse;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.repository.OrderConfirmationRepository;
import com.flexible.order.repository.OrderRepository;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.ReportItemServiceImpl;
import com.flexible.order.service.api.OrderService;
import com.flexible.order.service.conversion.DeliveryNotesToDtoConversionService;
import com.flexible.order.service.conversion.InvoiceToDtoConversionService;
import com.flexible.order.service.conversion.OrderConfirmationToDtoConversionService;
import com.flexible.order.service.conversion.OrderToDtoConversionService;
import com.flexible.order.service.conversion.ReportToDtoConversionService;
import com.flexible.order.web.dto.ReportDto;
import com.flexible.order.web.itextpdf.CreditNotePdfView;
import com.flexible.order.web.itextpdf.DeliveryNotesPdfView;
import com.flexible.order.web.itextpdf.InvoicePdfView;
import com.flexible.order.web.itextpdf.OrderConfirmationPdfView;
import com.flexible.order.web.itextpdf.OrderPdfView;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Controller for handling http requests on reports. E.g. PDFs
 * 
 * @author Marek Switajski
 * 
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    private static Logger log = Logger.getLogger(ReportController.class);
    @Autowired
    ReportRepository reportRepo;
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    ReportItemServiceImpl itemService;
    @Autowired
    CustomerRepository customerService;
    @Autowired
    OrderService orderService;
    @Autowired 
    ReportToDtoConversionService reportDtoConversionService;
    @Autowired 
    ReportToDtoConversionService creditNoteDtoConversionService;
    @Autowired 
    InvoiceToDtoConversionService invoiceDtoConversionService;
    @Autowired 
    DeliveryNotesToDtoConversionService deliveryNotesDtoConversionService;
    @Autowired 
    OrderConfirmationToDtoConversionService orderConfirmationDtoConversionService;
	@Autowired
    OrderToDtoConversionService orderDtoConversionService;
	@Autowired
	OrderConfirmationRepository orderConfirmationRepo;
    

    @RequestMapping(value = "/{id}.pdf", headers = "Accept=application/pdf")
    public ModelAndView showReportPdf(@PathVariable("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf; charset=utf-8");
        Report report = reportRepo.findByDocumentNumber(id);
        if (report != null){
        	return createReportSpecificModelAndView(report);
        } else if (orderConfirmationRepo.findByOrderAgreementNumber(id) != null){
            return createReportSpecificModelAndView(orderConfirmationRepo.findByOrderAgreementNumber(id));
        } else {
        	Order order = orderRepo.findByOrderNumber(id);
        	if (order != null)
        		return new ModelAndView(OrderPdfView.class.getSimpleName(),
                        ReportDto.class.getSimpleName(), orderDtoConversionService.toDto(order));
        }
        throw new IllegalArgumentException("Report or order with given id not found");
    }

    private ModelAndView createReportSpecificModelAndView(Report report) {
    	String model = ReportDto.class.getSimpleName();
        if (report instanceof OrderConfirmation) {
            return new ModelAndView(OrderConfirmationPdfView.class.getSimpleName(),
                    model, orderConfirmationDtoConversionService.toDto((OrderConfirmation) report));
        }
        if (report instanceof DeliveryNotes) {
            return new ModelAndView(DeliveryNotesPdfView.class.getSimpleName(),
                    model, deliveryNotesDtoConversionService.toDto((DeliveryNotes) report));
        }
        if (report instanceof Invoice) {
            return new ModelAndView(InvoicePdfView.class.getSimpleName(),
                    model, invoiceDtoConversionService.toDto((Invoice) report));
        }
        if (report instanceof CreditNote) {
            return new ModelAndView(CreditNotePdfView.class.getSimpleName(),
                    model, creditNoteDtoConversionService.toDto(report));
        }
        throw new IllegalStateException(
                "Could not find view handler for given Document");
    }

    @RequestMapping(value = "/listDocumentNumbersLike", method = RequestMethod.GET)
    public @ResponseBody
    JsonObjectResponse listInvoiceNumbers(
            @RequestParam(value = "query", required = false) Long orderNumberObject)
            throws Exception {

        log.debug("listOrderNumbers request:" + orderNumberObject);
        // TODO unify docNumbers
        throw new NotImplementedException();
    }

    public class invoiceNumber {

        public long invoiceNumber;

        public invoiceNumber(long invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
        }

    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public @ResponseBody
    JsonObjectResponse listAll(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts) {
        JsonObjectResponse response = new JsonObjectResponse();
        Page<Order> customer = orderRepo.findAll(new PageRequest(
                page - 1,
                limit));
        response.setData(customer.getContent());
        response.setTotal(customer.getTotalElements());
        response.setMessage("All entities retrieved.");
        response.setSuccess(true);

        return response;
    }

    @RequestMapping(value = "/orders/listOrderNumbers", method = RequestMethod.GET)
    public @ResponseBody
    JsonObjectResponse listOrderNumbers(
            @RequestParam(value = "orderNumber", required = false) String orderNumber,
            @RequestParam(value = "customerId", required = false) Long customerId)
            throws Exception {
        // FIXME: find by customer and orderNumber
        log.debug("listOrderNumbers request:" + orderNumber);
        JsonObjectResponse response = new JsonObjectResponse();

        if (orderNumber != null) {
            List<String> orderNumbers = itemService
                    .retrieveOrderNumbersLike(orderNumber);
            if (!orderNumbers.isEmpty()) {
                response.setTotal(orderNumbers.size());
                response.setData(formatOrderNumbers(orderNumbers));
            }
            else {
                response.setTotal(0l);
            }
        }
        else { // orderNumber == null
            List<Customer> customers = customerService.findAll();
            ArrayList<String> list = new ArrayList<String>();
            for (Customer customer : customers)
                list.addAll(itemService.retrieveOrderNumbersByCustomer(
                        customer,
                        new PageRequest(0, 20)).getContent());
            response.setTotal(list.size());
            response.setData(formatOrderNumbers(list));
        }

        response.setMessage("All entities retrieved.");
        response.setSuccess(true);

        return response;
    }

    private orderNumber[] formatOrderNumbers(List<String> orderNumbers) {
        List<orderNumber> entities = new ArrayList<orderNumber>();
        for (String on : orderNumbers)
            entities.add(new orderNumber(on));
        orderNumber[] orderNumberArray = entities.toArray(new orderNumber[0]);
        return orderNumberArray;
    }

    public class orderNumber {

        public String orderNumber;

        public orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

    }
}
