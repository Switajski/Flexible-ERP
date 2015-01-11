package com.flexible.order.web.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.deser.std.StdDeserializer.BigDecimalDeserializer;
import org.joda.time.LocalDate;

import com.flexible.order.json.EmptyStringStripToNullDeserializer;
import com.flexible.order.json.JsonDateDeserializer;
import com.flexible.order.json.JsonDateSerializer;
import com.flexible.order.json.JsonJodaLocalDateDeserializer;
import com.flexible.order.json.JsonJodaLocalDateSerializer;
import com.flexible.order.json.ProductTypeDeserializer;
import com.flexible.order.reference.ProductType;

/**
 * Data Transfer Object for ExtJs GUI </br> Build on
 * <code>BestellpositionData</code>, which is written in JavaScript.
 * 
 * @author Marek Switajski
 * 
 */
@JsonAutoDetect
public class ItemDto {

    public Long id;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    public Date created;
    public String product;
    public Long  
        customer, 
        customerNumber;
    
    @JsonDeserialize(using = EmptyStringStripToNullDeserializer.class)
    public String productName, 
        customerName, 
        orderNumber, 
        documentNumber, 
        invoiceNumber, 
        receiptNumber, 
        deliveryNotesNumber, 
        orderConfirmationNumber,
        orderAgreementNumber, 
        trackNumber, 
        packageNumber, 
        paymentConditions;

    public Integer quantity,
        quantityLeft;
    
    @JsonDeserialize(using = ProductTypeDeserializer.class)
    public ProductType productType;
    
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    public BigDecimal priceNet;
    
    public boolean shareHistory;

    public String status;
    
    @JsonSerialize(using = JsonJodaLocalDateSerializer.class)
    @JsonDeserialize(using = JsonJodaLocalDateDeserializer.class)
    public LocalDate expectedDelivery;

    public boolean agreed = true;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ItemDto other = (ItemDto) obj;
        if (id == null) {
            if (other.id != null) return false;
        }
        else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + quantityLeft + " x " + productName;
    }

}
