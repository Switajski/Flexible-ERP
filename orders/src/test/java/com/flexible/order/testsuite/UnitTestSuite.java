package com.flexible.order.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.flexible.order.application.AmountCalculatorTest;
import com.flexible.order.application.PurchaseAgreementServiceTest;
import com.flexible.order.application.ShippingCostsCalculatorTest;
import com.flexible.order.domain.ProductTest;
import com.flexible.order.service.OrderServiceTest;
import com.flexible.order.service.QuantityCalculatorTest;
import com.flexible.order.service.api.DeliveryServiceTest;
import com.flexible.order.service.api.InvoicingServiceTest;
import com.flexible.order.service.helper.StatusFilterDispatcherTest;
import com.flexible.order.web.JacksonDeserializationTest;
import com.flexible.order.web.helper.ProcessStepTest;

@RunWith(Suite.class)
@SuiteClasses({
		AmountCalculatorTest.class,
		JacksonDeserializationTest.class,
		OrderServiceTest.class,
		ProductTest.class,
		ShippingCostsCalculatorTest.class,
		QuantityCalculatorTest.class,
		PurchaseAgreementServiceTest.class,
		InvoicingServiceTest.class,
		DeliveryServiceTest.class,
		ProcessStepTest.class,
		StatusFilterDispatcherTest.class
})
public class UnitTestSuite {

}
