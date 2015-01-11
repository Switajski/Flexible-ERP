package com.flexible.order.repository.specification;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.report.ReportItem;

public class EmptyReportItemsSpecification implements Specification<OrderItem>{

	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		Path<Set<ReportItem>> reportItems = root.get("reportItems");
		Predicate emptyReportItemsPredicate = 
				cb.isEmpty(reportItems);
		return emptyReportItemsPredicate;
	}

	
}
