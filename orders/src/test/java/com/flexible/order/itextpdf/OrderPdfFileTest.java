package com.flexible.order.itextpdf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.Order;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.testhelper.AbstractSpringContextTest;
import com.flexible.order.testhelper.EntityBuilder.CatalogProductBuilder;
import com.flexible.order.testhelper.EntityBuilder.OrderItemBuilder;
import com.flexible.order.web.dto.ReportDto;
import com.flexible.order.web.itextpdf.OrderPdfFile;

public class OrderPdfFileTest {

	private final static String O_PDF_FILE = "src/test/resources/OrderPdfFileTest.pdf";

	private Order order;

	private ReportDto addOrderItems(ReportDto dto) {
		dto.orderItems = new HashSet<OrderItem>(Arrays.asList(
		new OrderItemBuilder(
				order,
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				5)
				.generateAttributes(15)
				.build(), 
		new OrderItemBuilder(
				order,
				CatalogProductBuilder
						.buildWithGeneratedAttributes(98760)
						.toProduct(),
				12)
				.generateAttributes(12)
				.build()));
		return dto;
	}

	@Transactional
	@Test
	public void shouldGenerateOrder() throws Exception {
		OrderPdfFile bpf = new OrderPdfFile();
		bpf.setFileNameAndPath(O_PDF_FILE);
		bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		ReportDto reportDto = addOrderItems(ReportDtoTestFixture.givenReportDto());
        model.put(reportDto.getClass().getSimpleName(), reportDto);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}
}
