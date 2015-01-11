package com.flexible.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flexible.order.domain.report.ReportItem;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, Long>, JpaSpecificationExecutor<ReportItem>{

}
