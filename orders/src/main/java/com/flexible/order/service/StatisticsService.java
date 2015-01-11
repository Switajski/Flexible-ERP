package com.flexible.order.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.repository.ReportItemRepository;
import com.flexible.order.service.helper.StatusFilterDispatcher;
import com.flexible.order.web.helper.ProductionState;

@Service
public class StatisticsService {

	@Autowired
	private ReportItemRepository reportItemRepo;
	
	@Autowired
	private StatusFilterDispatcher dispatcher;

	public Amount calculateOpenAmount(String state) {
		QuantityLeftCalculatorService calculator = new QuantityLeftCalculatorService();
		Set<OrderItem> calculated = new HashSet<OrderItem>();
		Amount summed = Amount.ZERO_EURO;

		List<ReportItem> ris = reportItemRepo
				.findAll(dispatcher.dispatchStatus(ProductionState.mapFromString(state)));

		for (ReportItem ri : ris) {
			if (calculated.add(ri.getOrderItem())){
				summed = summed.add(ri.getOrderItem().getNegotiatedPriceNet()
						.multiply(calculator.calculateLeft(ri)));
			}
		}
		return summed;
	}
}
