package com.flexible.order.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flexible.order.domain.Customer;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.reference.Country;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.testhelper.AbstractIntegrationTest;
import com.flexible.order.testhelper.EntityBuilder.CustomerBuilder;

public class CustomerIntegrationTest extends AbstractIntegrationTest<Customer> {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected Customer createEntity() {
		Long custNo = 15234L;

		return new CustomerBuilder(
				custNo,
				"email" + custNo + "nowhere.com",
				new Address(
						"name1", "name2",
						"street",
						1234, "city",
						Country.DEUTSCHLAND)).build();
	}

	@Override
	protected JpaRepository<Customer, Long> getRepository() {
		return customerRepository;
	}
}
