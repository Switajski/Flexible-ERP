package com.flexible.order.testhelper.EntityBuilder;

import com.flexible.order.domain.embeddable.CustomerDetails;
import com.flexible.order.domain.embeddable.PurchaseAgreement;
import com.flexible.order.domain.report.OrderConfirmation;

/**
 * @author Marek Switajski
 *
 */
public class OrderConfirmationBuilder extends ReportBuilder<OrderConfirmation, Builder<OrderConfirmation>>{

    private CustomerDetails customerDetails;
    private PurchaseAgreement purchaseAgreement;
    private String orderAgreementNumber;
    
	@Override
	public OrderConfirmation build() {
		OrderConfirmation dn = new OrderConfirmation();
		super.build(dn);
		dn.setPurchaseAgreement(purchaseAgreement);
		dn.setOrderAgreementNumber(orderAgreementNumber);
		dn.setCustomerDetails(customerDetails);
		return dn;
	}
	
	public OrderConfirmationBuilder withAB11(){
		setDocumentNumber("AB11");
		return this;
	}
	
	public OrderConfirmationBuilder setCustomerDetails(CustomerDetails customerDetails){
	    this.customerDetails = customerDetails;
	    return this;
	}

    public OrderConfirmationBuilder setAgreementDetails(PurchaseAgreement purchaseAgreement) {
        this.purchaseAgreement = purchaseAgreement;
        return this;
    }
    
    public OrderConfirmationBuilder setOrderAgreementNumber(String orderAgreementNumber) {
        this.orderAgreementNumber = orderAgreementNumber;
        return this;
    }
	
}
