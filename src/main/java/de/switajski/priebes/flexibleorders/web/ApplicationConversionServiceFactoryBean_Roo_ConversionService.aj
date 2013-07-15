// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.web;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ShippingItem;
import de.switajski.priebes.flexibleorders.service.ArchiveItemService;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.InvoiceItemService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.service.ProductService;
import de.switajski.priebes.flexibleorders.service.ShippingItemService;
import de.switajski.priebes.flexibleorders.web.ApplicationConversionServiceFactoryBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    @Autowired
    ArchiveItemService ApplicationConversionServiceFactoryBean.archiveItemService;
    
    @Autowired
    CustomerService ApplicationConversionServiceFactoryBean.customerService;
    
    @Autowired
    InvoiceItemService ApplicationConversionServiceFactoryBean.invoiceItemService;
    
    @Autowired
    OrderItemService ApplicationConversionServiceFactoryBean.orderItemService;
    
    @Autowired
    ProductService ApplicationConversionServiceFactoryBean.productService;
    
    @Autowired
    ShippingItemService ApplicationConversionServiceFactoryBean.shippingItemService;
    
    public Converter<ArchiveItem, String> ApplicationConversionServiceFactoryBean.getArchiveItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.ArchiveItem, java.lang.String>() {
            public String convert(ArchiveItem archiveItem) {
                return new StringBuilder().append(archiveItem.getCreated()).append(' ').append(archiveItem.getQuantity()).append(' ').append(archiveItem.getPriceNet()).append(' ').append(archiveItem.getProductName()).toString();
            }
        };
    }
    
    public Converter<Long, ArchiveItem> ApplicationConversionServiceFactoryBean.getIdToArchiveItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.ArchiveItem>() {
            public de.switajski.priebes.flexibleorders.domain.ArchiveItem convert(java.lang.Long id) {
                return archiveItemService.findArchiveItem(id);
            }
        };
    }
    
    public Converter<String, ArchiveItem> ApplicationConversionServiceFactoryBean.getStringToArchiveItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.ArchiveItem>() {
            public de.switajski.priebes.flexibleorders.domain.ArchiveItem convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ArchiveItem.class);
            }
        };
    }
    
    public Converter<Customer, String> ApplicationConversionServiceFactoryBean.getCustomerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.Customer, java.lang.String>() {
            public String convert(Customer customer) {
                return new StringBuilder().append(customer.getShortName()).append(' ').append(customer.getName1()).append(' ').append(customer.getName2()).append(' ').append(customer.getStreet()).toString();
            }
        };
    }
    
    public Converter<Long, Customer> ApplicationConversionServiceFactoryBean.getIdToCustomerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.Customer>() {
            public de.switajski.priebes.flexibleorders.domain.Customer convert(java.lang.Long id) {
                return customerService.findCustomer(id);
            }
        };
    }
    
    public Converter<String, Customer> ApplicationConversionServiceFactoryBean.getStringToCustomerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.Customer>() {
            public de.switajski.priebes.flexibleorders.domain.Customer convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Customer.class);
            }
        };
    }
    
    public Converter<InvoiceItem, String> ApplicationConversionServiceFactoryBean.getInvoiceItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.InvoiceItem, java.lang.String>() {
            public String convert(InvoiceItem invoiceItem) {
                return new StringBuilder().append(invoiceItem.getCreated()).append(' ').append(invoiceItem.getQuantity()).append(' ').append(invoiceItem.getPriceNet()).append(' ').append(invoiceItem.getProductName()).toString();
            }
        };
    }
    
    public Converter<Long, InvoiceItem> ApplicationConversionServiceFactoryBean.getIdToInvoiceItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.InvoiceItem>() {
            public de.switajski.priebes.flexibleorders.domain.InvoiceItem convert(java.lang.Long id) {
                return invoiceItemService.findInvoiceItem(id);
            }
        };
    }
    
    public Converter<String, InvoiceItem> ApplicationConversionServiceFactoryBean.getStringToInvoiceItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.InvoiceItem>() {
            public de.switajski.priebes.flexibleorders.domain.InvoiceItem convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), InvoiceItem.class);
            }
        };
    }
    
    public Converter<OrderItem, String> ApplicationConversionServiceFactoryBean.getOrderItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.OrderItem, java.lang.String>() {
            public String convert(OrderItem orderItem) {
                return new StringBuilder().append(orderItem.getCreated()).append(' ').append(orderItem.getQuantity()).append(' ').append(orderItem.getPriceNet()).append(' ').append(orderItem.getProductName()).toString();
            }
        };
    }
    
    public Converter<Long, OrderItem> ApplicationConversionServiceFactoryBean.getIdToOrderItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.OrderItem>() {
            public de.switajski.priebes.flexibleorders.domain.OrderItem convert(java.lang.Long id) {
                return orderItemService.findOrderItem(id);
            }
        };
    }
    
    public Converter<String, OrderItem> ApplicationConversionServiceFactoryBean.getStringToOrderItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.OrderItem>() {
            public de.switajski.priebes.flexibleorders.domain.OrderItem convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), OrderItem.class);
            }
        };
    }
    
    public Converter<Product, String> ApplicationConversionServiceFactoryBean.getProductToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.Product, java.lang.String>() {
            public String convert(Product product) {
                return new StringBuilder().append(product.getProductNumber()).append(' ').append(product.getName()).append(' ').append(product.getSortOrder()).append(' ').append(product.getIntro()).toString();
            }
        };
    }
    
    public Converter<Long, Product> ApplicationConversionServiceFactoryBean.getIdToProductConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.Product>() {
            public de.switajski.priebes.flexibleorders.domain.Product convert(java.lang.Long id) {
                return productService.findProduct(id);
            }
        };
    }
    
    public Converter<String, Product> ApplicationConversionServiceFactoryBean.getStringToProductConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.Product>() {
            public de.switajski.priebes.flexibleorders.domain.Product convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Product.class);
            }
        };
    }
    
    public Converter<ShippingItem, String> ApplicationConversionServiceFactoryBean.getShippingItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<de.switajski.priebes.flexibleorders.domain.ShippingItem, java.lang.String>() {
            public String convert(ShippingItem shippingItem) {
                return new StringBuilder().append(shippingItem.getCreated()).append(' ').append(shippingItem.getQuantity()).append(' ').append(shippingItem.getPriceNet()).append(' ').append(shippingItem.getProductName()).toString();
            }
        };
    }
    
    public Converter<Long, ShippingItem> ApplicationConversionServiceFactoryBean.getIdToShippingItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, de.switajski.priebes.flexibleorders.domain.ShippingItem>() {
            public de.switajski.priebes.flexibleorders.domain.ShippingItem convert(java.lang.Long id) {
                return shippingItemService.findShippingItem(id);
            }
        };
    }
    
    public Converter<String, ShippingItem> ApplicationConversionServiceFactoryBean.getStringToShippingItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, de.switajski.priebes.flexibleorders.domain.ShippingItem>() {
            public de.switajski.priebes.flexibleorders.domain.ShippingItem convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ShippingItem.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getArchiveItemToStringConverter());
        registry.addConverter(getIdToArchiveItemConverter());
        registry.addConverter(getStringToArchiveItemConverter());
        registry.addConverter(getCustomerToStringConverter());
        registry.addConverter(getIdToCustomerConverter());
        registry.addConverter(getStringToCustomerConverter());
        registry.addConverter(getInvoiceItemToStringConverter());
        registry.addConverter(getIdToInvoiceItemConverter());
        registry.addConverter(getStringToInvoiceItemConverter());
        registry.addConverter(getOrderItemToStringConverter());
        registry.addConverter(getIdToOrderItemConverter());
        registry.addConverter(getStringToOrderItemConverter());
        registry.addConverter(getProductToStringConverter());
        registry.addConverter(getIdToProductConverter());
        registry.addConverter(getStringToProductConverter());
        registry.addConverter(getShippingItemToStringConverter());
        registry.addConverter(getIdToShippingItemConverter());
        registry.addConverter(getStringToShippingItemConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
