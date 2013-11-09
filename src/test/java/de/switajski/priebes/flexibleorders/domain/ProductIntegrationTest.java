package de.switajski.priebes.flexibleorders.domain;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import de.switajski.priebes.flexibleorders.integrationtest.AbstractIntegrationTest;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.repository.CategoryRepository;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.CategoryBuilder;
import de.switajski.priebes.flexibleorders.test.EntityBuilder.ProductBuilder;

public class ProductIntegrationTest extends AbstractIntegrationTest<Product> {

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CategoryRepository catRepo;
	
	@Override
	protected Product createEntity() {
		Product product = 
				new ProductBuilder(
						new Category(), 12345L, ProductType.PRODUCT)
			.build();
		return product;
	}

	@Override
	protected JpaRepository<Product, Long> getRepository() {
		return productRepo;
	}
	
	@Test
	public void shouldFindByName(){
		String name = "name o name";
		Category cat = new CategoryBuilder("asfd", true).build();
		catRepo.saveAndFlush(cat);
		
		Product p = createEntity();
		p.setName(name);
		p.setActive(true);
		p.setCategory(cat);
		
		productRepo.save(p);
		productRepo.flush();
		
		assertNotNull(productRepo.findByName(name));
	}

}
