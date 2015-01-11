package com.flexible.order.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import com.flexible.order.application.DeliveryHistory;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.service.helper.StringUtil;

@JsonAutoDetect
public class DeliveryHistoryDto extends ArrayList<Map<String, Object>> {

    private static final long serialVersionUID = 1L;

    public DeliveryHistoryDto(DeliveryHistory deliveryHistory) {
        this.add(createChild(
                StringUtil.concatWithCommas(deliveryHistory.getOrderNumbers()),
                createOrderString(deliveryHistory),
                true));
        for (ReportItem ri : deliveryHistory.getItemsSorted()) {
            Map<String, Object> child = createChild(
                    ri.getReport().getDocumentNumber(),
                    ri.getReport().getDocumentNumber() + ": "
                            + ri.getQuantity(),
                    true);
            this.add(child);
        }
    }

    private Map<String, Object> createChild(String id, String text, boolean leaf) {
        Map<String, Object> child = new HashMap<String, Object>();
        child.put("id", id);
        child.put("text", text);
        child.put("leaf", true);
        return child;
    }

    public String createOrderString(DeliveryHistory dh) {
        OrderItem oi = dh.getItems().iterator().next().getOrderItem();
        return StringUtil.concatWithCommas(dh.getOrderNumbers()) + 
                ": " + oi.getOrderedQuantity() + " x " + oi.getProduct().getName();
    }

}
