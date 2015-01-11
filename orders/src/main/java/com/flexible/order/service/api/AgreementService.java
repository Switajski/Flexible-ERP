package com.flexible.order.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flexible.order.domain.report.OrderConfirmation;
import com.flexible.order.itextpdf.builder.Unicode;
import com.flexible.order.repository.OrderConfirmationRepository;

@Service
public class AgreementService {

    @Autowired
    private OrderConfirmationRepository reportRepo;

    @Transactional
    public OrderConfirmation agree(String orderConfirmationNo, String orderAgreementNo) {
        OrderConfirmation oc = reportRepo.findByDocumentNumber(orderConfirmationNo);
        if (oc == null) {
            throw new IllegalArgumentException("Auftragsbest" + Unicode.aUml + "tigung mit angegebener Nummer nicht gefunden");
        }
        oc.setOrderAgreementNumber(orderAgreementNo);
        return reportRepo.save(oc);
    }

}
