package com.flexible.order.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.flexible.order.domain.report.ReceiptItem;
import com.flexible.order.domain.report.ReportItem;

public class ReceiptItemCompletedSpec implements Specification<ReportItem>{

	@Override
	public Predicate toPredicate(Root<ReportItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		//TODO: this will return items, that are only partially completed
		return cb.equal(root.type(), ReceiptItem.class);
	}

}
