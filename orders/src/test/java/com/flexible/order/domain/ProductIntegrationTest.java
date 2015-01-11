package com.flexible.order.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flexible.order.domain.CatalogProduct;
import com.flexible.order.domain.Category;
import com.flexible.order.repository.CatalogProductRepository;
import com.flexible.order.repository.CategoryRepository;
import com.flexible.order.testhelper.AbstractIntegrationTest;
import com.flexible.order.testhelper.EntityBuilder.CatalogProductBuilder;
import com.flexible.order.testhelper.EntityBuilder.CategoryBuilder;

public class ProductIntegrationTest extends
		AbstractIntegrationTest<CatalogProduct> {

	@Autowired
	private CatalogProductRepository productRepo;

	@Autowired
	private CategoryRepository catRepo;

	@Override
	protected CatalogProduct createEntity() {
		CatalogProduct product =
				CatalogProductBuilder.buildWithGeneratedAttributes(901834675);
		return product;
	}

	@Override
	protected JpaRepository<CatalogProduct, Long> getRepository() {
		return productRepo;
	}

	@Test
	public void shouldFindByName() {
		String name = "name o name";
		Category cat = new CategoryBuilder("asfd", true).build();
		catRepo.saveAndFlush(cat);

		CatalogProduct p = createEntity();
		p.setName(name);

		productRepo.save(p);
		productRepo.flush();

		assertNotNull(productRepo.findByName(name));
	}

}
