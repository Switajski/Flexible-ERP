package com.flexible.order.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flexible.order.domain.CatalogProduct;

@Repository
public interface CatalogProductRepository extends JpaSpecificationExecutor<CatalogProduct>, JpaRepository<CatalogProduct, Long> {
	
	CatalogProduct findByProductNumber(String productNumber);
	
	CatalogProduct findByName(String name);
	
}
