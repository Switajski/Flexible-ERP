package com.flexible.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Report;

@Repository
public interface OrderConfirmationRepository extends JpaRepository<OrderConfirmation, String>, JpaSpecificationExecutor<Report>{

	OrderConfirmation findByOrderAgreementNumber(String orderAgreementNumber);

    OrderConfirmation findByDocumentNumber(String orderConfirmationNo);

}
