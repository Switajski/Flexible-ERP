package com.flexible.order.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flexible.order.domain.Category;
import com.flexible.order.repository.CategoryRepository;
import com.flexible.order.testhelper.AbstractIntegrationTest;
import com.flexible.order.testhelper.EntityBuilder.CategoryBuilder;

public class CategoryIntegrationTest extends AbstractIntegrationTest<Category> {

	@Autowired
	private CategoryRepository catRepo;

	@Override
	protected Category createEntity() {
		return CategoryBuilder.buildWithGeneratedAttributes(3146);
	}

	@Override
	protected JpaRepository<Category, Long> getRepository() {
		return catRepo;
	}
}
