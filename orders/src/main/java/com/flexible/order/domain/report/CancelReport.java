package com.flexible.order.domain.report;

import javax.persistence.Entity;

@Entity
public class CancelReport extends Report {

	protected CancelReport() {

	}

	public CancelReport(String documentNumber) {
		super(documentNumber);
	}

}
