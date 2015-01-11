package com.flexible.order.testhelper.EntityBuilder;

import com.flexible.order.domain.Product;
import com.flexible.order.reference.ProductType;

/**
 * 
 * @author Marek Switajski
 *
 */
public class ProductBuilder implements Builder<Product>{

	private String productNumber;

	private ProductType productType;

	private String name;

	@Override
	public Product build() {
		Product product = new Product();
		product.setProductNumber(productNumber);
		product.setProductType(productType);
		product.setName(name);
		return product;
	}

	public ProductBuilder setProductNumber(String productNumber) {
		this.productNumber = productNumber;
		return this;
	}

	public ProductBuilder setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	public ProductBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
}
