package com.flexible.order.application.process;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.flexible.order.domain.report.ConfirmationItem;
import com.flexible.order.domain.report.DeliveryNotes;
import com.flexible.order.domain.report.Invoice;
import com.flexible.order.domain.report.InvoiceItem;
import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.domain.report.Receipt;
import com.flexible.order.domain.report.ReceiptItem;
import com.flexible.order.domain.report.Report;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.domain.report.ShippingItem;

/**
 * 
 * @deprecated Too complicated - use specific Items instead
 * @author Marek Switajski
 *
 */
public class WholesaleProcessSteps {

	public static List<Class<? extends ReportItem>> reportItemSteps() {
		List<Class<? extends ReportItem>> reportItemOrder = new ArrayList<Class<? extends ReportItem>>();
		reportItemOrder.add(ConfirmationItem.class);
		reportItemOrder.add(ShippingItem.class);
		reportItemOrder.add(InvoiceItem.class);
		reportItemOrder.add(ReceiptItem.class);
		return reportItemOrder;
	}

	public static List<Class<? extends Report>> reportSteps() {
		List<Class<? extends Report>> reportOrder = new ArrayList<Class<? extends Report>>();
		reportOrder.add(OrderConfirmation.class);
		reportOrder.add(DeliveryNotes.class);
		reportOrder.add(Invoice.class);
		reportOrder.add(Receipt.class);
		return reportOrder;
	}

	public static Class<? extends ReportItem> getPreviousReportItemStep(
			Class<? extends ReportItem> clazz) {
		ListIterator<Class<? extends ReportItem>> lItr = getReportItemIteratorFor(clazz);
		if (lItr.hasPrevious())
			return lItr.previous();
		return null;
	}

	public static Class<? extends Report> getPreviousReportStep(
			Class<? extends Report> clazz) {
		ListIterator<Class<? extends Report>> lItr = getReportStepIteratorFor(clazz);
		if (lItr.hasPrevious())
			return lItr.previous();
		return null;
	}

	public static Class<? extends ReportItem> getNextReportItemStep(
			Class<? extends ReportItem> clazz) {
		ListIterator<Class<? extends ReportItem>> lItr = getReportItemIteratorFor(clazz);
		if (lItr.hasNext())
			return lItr.next();
		return null;
	}

	private static ListIterator<Class<? extends ReportItem>> getReportItemIteratorFor(
			Class<? extends ReportItem> clazz) {
		ListIterator<Class<? extends ReportItem>> lItr = reportItemSteps()
				.listIterator(reportItemSteps().indexOf(clazz));
		return lItr;
	}
	
	private static ListIterator<Class<? extends Report>> getReportStepIteratorFor(
			Class<? extends Report> clazz) {
		ListIterator<Class<? extends Report>> lItr = reportSteps()
				.listIterator(reportItemSteps().indexOf(clazz));
		return lItr;
	}

}
