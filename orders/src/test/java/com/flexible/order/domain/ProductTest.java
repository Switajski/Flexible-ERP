package com.flexible.order.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.flexible.order.domain.Product;
import com.flexible.order.testhelper.EntityBuilder.CatalogProductBuilder;

public class ProductTest {

	@Test
	public void equals_ifEqualProductsShouldReturnTrue(){
		// GIVEN
		Product amy1 = new CatalogProductBuilder().amy().build().toProduct();
		Product amy2 = new CatalogProductBuilder().amy().build().toProduct();
		
		//WHEN / THEN
		assertThat(amy1.equals(amy2), is(true));
	}
}
