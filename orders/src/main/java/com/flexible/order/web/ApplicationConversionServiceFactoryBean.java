package com.flexible.order.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Service;

import com.flexible.order.domain.Customer;
import com.flexible.order.domain.OrderItem;
import com.flexible.order.domain.Product;
import com.flexible.order.repository.CatalogProductRepository;
import com.flexible.order.repository.CustomerRepository;
import com.flexible.order.repository.OrderItemRepository;
import com.flexible.order.service.CustomerServiceImpl;
import com.flexible.order.service.ReportItemServiceImpl;

/**
 * A central place to register application converters and formatters. 
 */
@Service
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
//		registry.addConverter(getJSONCollectionToListConverter());
		//TODO: centralized JSON-Converter
	}
	
    @Autowired
    CustomerServiceImpl customerService;
    
    @Autowired
    CustomerRepository customerRepo;
    
    @Autowired
    ReportItemServiceImpl itemService;
    
    @Autowired
    OrderItemRepository itemRepo;
    
    @Autowired
    CatalogProductRepository productService;

    //TODO: install missing converters
    
    public Converter<Customer, String> getCustomerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.flexible.order.domain.Customer, java.lang.String>() {
            public String convert(Customer customer) {
                return new StringBuilder().append(customer.getCustomerNumber()).append(' ').append(customer.getFirstName()).append(' ').append(customer.getLastName()).append(' ').append(customer.getInvoiceAddress().getStreet()).toString();
            }
        };
    }
    
    public Converter<Long, Customer> getIdToCustomerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.flexible.order.domain.Customer>() {
            public com.flexible.order.domain.Customer convert(java.lang.Long id) {
                return customerRepo.findOne(id);
            }
        };
    }
    
    public Converter<String, Customer> getStringToCustomerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.flexible.order.domain.Customer>() {
            public com.flexible.order.domain.Customer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Customer.class);
            }
        };
    }
    
    public Converter<OrderItem, String> getOrderItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.flexible.order.domain.OrderItem, java.lang.String>() {
            public String convert(OrderItem orderItem) {
                return new StringBuilder().append(orderItem.getCreated()).append(' ').append(orderItem.getOrderedQuantity()).append(' ').append(' ').append(orderItem.getProduct().getName()).toString();
            }
        };
    }
    
    public Converter<Long, OrderItem> getIdToOrderItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.flexible.order.domain.OrderItem>() {
            public com.flexible.order.domain.OrderItem convert(java.lang.Long id) {
                return itemRepo.findOne(id);
            }
        };
    }
    
    public Converter<String, OrderItem> getStringToOrderItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.flexible.order.domain.OrderItem>() {
            public com.flexible.order.domain.OrderItem convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), OrderItem.class);
            }
        };
    }
    
    public Converter<Product, String> getProductToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.flexible.order.domain.Product, java.lang.String>() {
            public String convert(Product product) {
                return new StringBuilder().append(product.getProductNumber()).append(' ').append(product.getName()).toString();
            }
        };
    }
    
//    public Converter<Long, Product> getIdToProductConverter() {
//        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.Product>() {
//            public de.switajski.priebes.flexibleorders.domain.Product convert(java.lang.Long id) {
//                return productService.findProduct(id);
//            }
//        };
//    }
    
    public Converter<String, Product> getStringToProductConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.flexible.order.domain.Product>() {
            public com.flexible.order.domain.Product convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Product.class);
            }
        };
    }
    
    public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getCustomerToStringConverter());
        registry.addConverter(getIdToCustomerConverter());
        registry.addConverter(getStringToCustomerConverter());
        registry.addConverter(getStringToOrderItemConverter());
        registry.addConverter(getProductToStringConverter());
        registry.addConverter(getStringToProductConverter());
    }
    
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
	
}
