package com.flexible.order.itextpdf;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.flexible.order.web.dto.ReportDto;
import com.flexible.order.web.itextpdf.DeliveryNotesPdfFile;

public class DeliveryNotesPdfFileTest{

	private static final String PDF_PATH = "src/test/resources/DeliveryNotesPdfFileTest.pdf";

	@Test
	public void shouldGenerateInvoice() throws Exception {

		DeliveryNotesPdfFile bpf = new DeliveryNotesPdfFile();
		bpf.setFilePathAndName(PDF_PATH);
		bpf.setLogoPath("C:/workspaces/gitRepos/FlexibleOrders/src/main/webapp/images/LogoGross.jpg");

		Map<String, Object> model = new HashMap<String, Object>();
		ReportDto reportDto = ReportDtoTestFixture.givenReportDto();
		model.put(reportDto.getClass().getSimpleName(), reportDto);

		bpf.render(
				model,
				new MockHttpServletRequest(),
				new MockHttpServletResponse());

	}
}
