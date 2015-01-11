package com.flexible.order.web;

import com.flexible.order.web.dto.JsonCreateReportRequest;

public class InvoicingParameter {
    public JsonCreateReportRequest deliverRequest;

    public InvoicingParameter(JsonCreateReportRequest deliverRequest) {
        this.deliverRequest = deliverRequest;
    }
}