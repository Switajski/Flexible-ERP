package com.flexible.order.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.flexible.order.repository.CatalogProductRepository;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.repository.OrderItemRepository;
import com.flexible.order.service.ImporterService;
import com.flexible.order.testhelper.AbstractSpringContextTest;

public class PriebesJoomlaImporterServiceTest extends AbstractSpringContextTest{

	@Autowired
	ImporterService pji;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CatalogProductRepository productRepository;
	@Autowired
	OrderItemRepository itemRepository;

	@Rollback(false)
	@Test
	public void shouldImportOrderItems() {
		// pji.importCustomers();
		// customerRepository.flush();
		// assertFalse("OrderItemRepository repository is empty!",
		// customerRepository.count()==0);

		pji.importProducts();
		pji.importPrices();
		assertFalse(
				"OrderItemRepository repository is empty!",
				productRepository.count() == 0);

		// pji.importOrderItems();
		// assertFalse("OrderItemRepository repository is empty!",
		// itemRepository.count()==0);
	}

}
