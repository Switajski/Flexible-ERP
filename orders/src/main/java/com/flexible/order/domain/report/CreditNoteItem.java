package com.flexible.order.domain.report;

import javax.persistence.Entity;

@Entity
public class CreditNoteItem extends ReportItem {

    @Override
    public String provideStatus() {
        return "gutgeschrieben";
    }

}
