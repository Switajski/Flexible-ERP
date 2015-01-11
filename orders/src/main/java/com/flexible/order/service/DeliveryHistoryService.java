package com.flexible.order.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.application.DeliveryHistory;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.domain.report.ShippingItem;
import com.flexible.order.repository.OrderItemRepository;
import com.flexible.order.repository.ReportItemRepository;
import com.flexible.order.web.dto.DeliveryHistoryDto;

@Service
public class DeliveryHistoryService {

	@Autowired
	private ReportItemRepository riRepo;
	
	@Autowired
	private OrderItemRepository oiRepo;
	
	@Transactional(readOnly=true)
	public DeliveryHistoryDto retrieveByReportItemId(Long itemDtoId){
		ReportItem ri = riRepo.findOne(itemDtoId);
		if (ri == null){
			throw new IllegalArgumentException("ReportItem with given id " + itemDtoId + " not found");
		}
		return new DeliveryHistoryDto(DeliveryHistory.of(ri));
	}
	
	@Transactional(readOnly=true)
	public DeliveryHistoryDto retrieveByOrderItemId(Long itemDtoId){
		OrderItem oi = oiRepo.findOne(itemDtoId);
		if (oi == null){
			throw new IllegalArgumentException("OrderItem with given id not found");
		}
		return new DeliveryHistoryDto(DeliveryHistory.of(oi));
	}

	/**
	 * FIXME this method returns also DeliveryNotes of 
	 * @param reportItems
	 * @return
	 */
	@Transactional(readOnly=true)
    public Collection<DeliveryNotes> retrieveDeliveryNotesFrom(Report report) {
        DeliveryHistory dh = DeliveryHistory.of(report);
        HashSet<DeliveryNotes> dns = new HashSet<DeliveryNotes>();
        for (ReportItem ri:dh.getReportItems(ShippingItem.class)){
            dns.add((DeliveryNotes) ri.getReport());
        }
        return dns;
    }
	
}
