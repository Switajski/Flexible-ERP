package com.flexible.order.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.flexible.order.domain.CategoryIntegrationTest;
import com.flexible.order.domain.CustomerIntegrationTest;
import com.flexible.order.domain.ProductIntegrationTest;
import com.flexible.order.itextpdf.DeliveryNotesPdfFileTest;
import com.flexible.order.itextpdf.InvoicePdfFileTest;
import com.flexible.order.itextpdf.OrderConfirmationPdfFileTest;
import com.flexible.order.itextpdf.OrderPdfFileTest;
import com.flexible.order.service.SpecificationIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({
		CategoryIntegrationTest.class,
		OrderConfirmationPdfFileTest.class,
		CustomerIntegrationTest.class,
		DeliveryNotesPdfFileTest.class,
		InvoicePdfFileTest.class,
		OrderPdfFileTest.class,
		ProductIntegrationTest.class,
		SpecificationIntegrationTest.class
})
public class IntegrationTestSuite {

}
