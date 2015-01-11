package com.flexible.order.service.helper;

import java.util.ArrayList;
import java.util.List;

import com.flexible.order.domain.report.Report;

public abstract class ReportFilterHelper {

	public static List<Report> filter(List<Report> reports, Class<? extends Report> clazz){
		List<Report> returnedReports = new ArrayList<Report>();
		for (Report r: reports){
			if (clazz.isInstance(r))
				returnedReports.add(r);
		}
		return returnedReports;
	}
	
}
