package de.switajski.priebes.flexibleorders.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.reference.Status;

@RooJavaBean
@RooToString
@RooJpaEntity
public class OrderItem extends Item {

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date expectedDelivery;

    /**
     */
    @Column(unique=true)
    @NotNull
    private int orderItemNumber;

    /**
     * 
     * @param toSupplier
     * @return
     */
	public ShippingItem confirm(boolean toSupplier) {
		ShippingItem si = new ShippingItem(this, toSupplier);
		this.setStatus(Status.CONFIRMED);
		return si;
	}

	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
