package com.flexible.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.Customer;
import com.flexible.order.domain.Order;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.repository.OrderRepository;
import com.flexible.order.repository.ReportItemRepository;
import com.flexible.order.repository.ReportRepository;
import com.flexible.order.service.conversion.ItemDtoConverterService;
import com.flexible.order.service.conversion.ReportItemToItemDtoPageConverterService;
import com.flexible.order.web.dto.ItemDto;

@Service
public class ReportItemServiceImpl {

	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private ReportRepository reportRepo;
	@Autowired
	private ReportItemRepository reportItemRepo;
	@Autowired
	private ReportItemToItemDtoPageConverterService pageConverterService;
	@Autowired
	private ItemDtoConverterService itemDtoConverterService;

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmedByCustomer(Customer customer,
			Pageable pageable) {
		Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmedByCustomer(
				customer,
				pageable);
		return pageConverterService.createPage(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmed(PageRequest pageable) {
		Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmed(pageable);
		return pageConverterService.createPage(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
	}

	@Transactional(readOnly = true)
	public List<String> retrieveOrderNumbersLike(String orderNumber) {
		List<Order> orders = orderRepo.findByOrderNumberLike(orderNumber);
		return extractOrderNumbers(orders);
	}

	private List<String> extractOrderNumbers(List<Order> orders) {
		List<String> orderNumbers = new ArrayList<String>();
		for (Order order : orders) {
			orderNumbers.add(order.getOrderNumber());
		}
		return orderNumbers;
	}

	@Transactional(readOnly = true)
	public Page<String> retrieveOrderNumbersByCustomer(Customer customer,
			PageRequest pageRequest) {

		Page<Order> orders = orderRepo.findByCustomer(customer, pageRequest);
		Page<String> result = extractOrderNumber(orders);
		return result;
	}

	private Page<String> extractOrderNumber(Page<Order> orders) {
		// if no orders are found return empty list
		if (orders.getSize() < 1)
			return new PageImpl<String>(new ArrayList<String>());

		List<String> ordersList = new ArrayList<String>();
		for (Order order : orders)
			ordersList.add(order.getOrderNumber());

		Page<String> result = new PageImpl<String>(
				ordersList, new PageRequest(
						orders.getSize(),
						orders.getNumber() + 1),
				orders.getTotalElements()
				);
		return result;
	}

	@Transactional(readOnly = true)
	public List<ItemDto> retrieveAllByDocumentNumber(String string) {
		if (string == null)
			throw new IllegalArgumentException(
					"Dokumentennummer nicht angegeben");
		Report report = reportRepo.findByDocumentNumber(string);
		List<ItemDto> reportItems = new ArrayList<ItemDto>();
		for (ReportItem he : report.getItems())
			reportItems.add(itemDtoConverterService.convert(he));
		return reportItems;
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieve(PageRequest pageRequest, Specification<ReportItem> spec){
		Page<ReportItem> openReportItems = reportItemRepo.findAll(spec, pageRequest);
		return pageConverterService.createWithWholeNonCompletedReports(pageRequest, openReportItems);
	}
	
}
