package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.helper.AmountCalculator;
import de.switajski.priebes.flexibleorders.reference.ProductType;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.report.itextpdf.builder.PdfPTableBuilder;

@Component
public class InvoicePdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Invoice report = (Invoice) model.get(Invoice.class.getSimpleName());
		
		//TODO
//		String rightTop = "Kundennummer: " + "TODO";
		String rightTop = "";
		String rightBottom = "Kundennummer: " + report.getCustomerNumber();
		String leftTop = "Rechnungsnummer: " + report.getDocumentNumber().toString();
		String leftBottom = "Rechnungsdatum: " + dateFormat.format(report.getCreated());
		Address adresse = report.getInvoiceAddress();
		String heading = "Rechnung";
		
		Amount vat = AmountCalculator.calculateVatAmount(report, ConfirmationReportPdfView.VAT_RATE);
        Amount net = AmountCalculator.calculateNetAmount(report);
        Amount shippingCosts = report.getShippingCosts();
        Amount gross = net.add(vat);
		gross = gross.add(shippingCosts);

		// insert address
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
		if (adresse == null) {
			document.add(ParagraphBuilder.createEmptyLine());
			document.add(ParagraphBuilder.createEmptyLine());
			document.add(ParagraphBuilder.createEmptyLine());
		} else {
			document.add(new ParagraphBuilder(adresse.getName1())
			.withIndentationLeft(36f)
			.withLineSpacing(12f)
			.addTextLine(adresse.getName2())
			.addTextLine(adresse.getStreet())
			.addTextLine(adresse.getPostalCode() + " " + adresse.getCity())
			.addTextLine(adresse.getCountry().toString())
			.build());
		}
		document.add(ParagraphBuilder.createEmptyLine());
		document.add(ParagraphBuilder.createEmptyLine());
        
		
        // insert heading
		document.add(new ParagraphBuilder(heading)
		.withFont(FontFactory.getFont(FONT, 12, Font.BOLD))
		.build());
		document.add(ParagraphBuilder.createEmptyLine());
		

		// info table
		CustomPdfPTableBuilder infoTableBuilder = CustomPdfPTableBuilder.createInfoTable(
        		leftTop, leftBottom, rightTop, rightBottom);
        PdfPTable infoTable = infoTableBuilder.build();
		infoTable.setWidthPercentage(100);
		document.add(infoTable);
        //TODO: if (auftragsbestaetigung.getAusliefDatum==null) insertInfo(document,"Voraussichtliches Auslieferungsdatum:" + auftragsbestaetigung.getGeplAusliefDatum());
        document.add(ParagraphBuilder.createEmptyLine());

        
        // insert main table
        document.add(createTable(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder.createFooterBuilder(
				net, vat, shippingCosts, gross, report.getPaymentConditions())
				.withTotalWidth(PriebesIText5PdfView.WIDTH);
	    
	    PdfPTable footer = footerBuilder.build();
	    
	    footer.writeSelectedRows(0, -1,
	    		/*xPos*/ PriebesIText5PdfView.PAGE_MARGIN_LEFT, 
	    		/*yPos*/ PriebesIText5PdfView.PAGE_MARGIN_BOTTOM + FOOTER_MARGIN_BOTTOM, 
	    		writer.getDirectContent());
	}
	//TODO: make it an invoiceTable
//	private PdfPTable createTable(Invoice invoice, Document doc) throws DocumentException{
//		PdfPTableBuilder builder = PdfPTableBuilder.buildWithFourCols();
//		for (HandlingEvent he: invoice.getEvents()){
//			if (!he.getOrderItem().isShippingCosts()){
//				builder.addBodyRow(
//						new FourStrings("",
//								// product Name
//								"Art.Nr.: " + he.getOrderItem().getProduct().getProductNumber() + " - "
//								+ he.getOrderItem().getProduct().getName(),
//								// price
//								he.getQuantity()+ " x " + he.getOrderItem().getNegotiatedPriceNet().toString(),
//								// amount of single item
//								he.getOrderItem().getNegotiatedPriceNet().multiply(he.getQuantity()).toString()
//								));
//			}
//		}
//		Amount net = AmountCalculator.calculateNetAmount(invoice);
//		//TODO: make vatRate dependent from order
//		Amount vat = AmountCalculator.calculateVatAmount(invoice, VAT_RATE);
//		builder.addFooterRow("Warenwert netto:   "+ net.toString())
//		.addFooterRow("zzgl. 19% MwSt.:     " + vat.toString());
//		
//		Amount shippingCosts = new Amount();
//		for (Entry<String, Amount> shipment:invoice.getShippingCosts().entrySet()){
//			Amount price = shipment.getValue();
//			if (!price.isGreaterZero())
//				throw new IllegalStateException("Versand ohne Preis angegeben!");
//			builder.addFooterRow("Versandkosten aus Lieferschein " + shipment.getKey() +":     " + price.toString());
//			shippingCosts = shippingCosts.add(price);
//		}
//		net = net.add(vat);
//		net = net.add(shippingCosts);
//		builder.addFooterRow("Gesamtbetrag brutto:   " + 
//				net.toString());
//		return builder.build();
//	}
	
	private PdfPTable createTable(Report cReport) throws DocumentException{
		PdfPTableBuilder builder = new PdfPTableBuilder(PdfPTableBuilder.createPropertiesWithSixCols());
		for (HandlingEvent he: cReport.getEvents()){
			if (he.getOrderItem().getProduct().getProductType() != ProductType.SHIPPING){
				List<String> list = new ArrayList<String>();
				// Art.Nr.:
				if (he.getOrderItem().getProduct().getProductNumber()!= null)
					list.add(he.getOrderItem().getProduct().getProductNumber().toString());
				else list.add("n.a.");
				// Artikel
				list.add(he.getOrderItem().getProduct().getName());
				// Anzahl
				list.add(String.valueOf(he.getQuantity()));
				// EK per Stueck
				list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
				// Bestellnr
				list.add(he.getOrderItem().getOrder().getOrderNumber());
				// gesamt
				list.add(he.getOrderItem().getNegotiatedPriceNet().multiply(he.getQuantity()).toString());

				builder.addBodyRow(list);
			}
		}
		
		return builder.withFooter(false).build();
	}
	
	
}
