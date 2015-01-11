package com.flexible.order.service.helper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import com.flexible.order.domain.report.ReportItem;
import com.flexible.order.service.helper.StatusFilterDispatcher;
import com.flexible.order.web.helper.ProductionState;

public class StatusFilterDispatcherTest {

    @Test
    public void shouldDispatchStatusToSpecification() throws Exception{
        // WHEN
        Specification<ReportItem> spec = new StatusFilterDispatcher().dispatchStatus(ProductionState.values()[0]);
        
        // THEN
        assertThat(spec, is(not(nullValue())));
    }
    
}
