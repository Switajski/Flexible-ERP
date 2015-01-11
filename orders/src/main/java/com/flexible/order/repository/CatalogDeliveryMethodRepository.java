package com.flexible.order.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flexible.order.domain.CatalogDeliveryMethod;

@Repository
public interface CatalogDeliveryMethodRepository extends JpaSpecificationExecutor<CatalogDeliveryMethod>, JpaRepository<CatalogDeliveryMethod, Long> {
	
}
