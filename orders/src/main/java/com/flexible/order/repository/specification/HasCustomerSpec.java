package com.flexible.order.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.flexible.order.domain.Customer;
import com.flexible.order.domain.report.ReportItem;

public class HasCustomerSpec implements Specification<ReportItem> {
	
	private Customer customer;

	public HasCustomerSpec(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Predicate toPredicate(Root<ReportItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		return cb.equal(root.get("orderItem").get("customerOrder").get("customer"), customer);
	}

}
