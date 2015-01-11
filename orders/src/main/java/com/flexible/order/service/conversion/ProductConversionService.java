package com.flexible.order.service.conversion;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.flexible.order.domain.CatalogProduct;
import com.flexible.order.domain.embeddable.Amount;
import com.flexible.order.reference.Currency;
import com.flexible.order.reference.ProductType;
import com.flexible.order.web.dto.MagentoProductDto;

@Service
public class ProductConversionService {

    public Set<CatalogProduct> convert(Collection<MagentoProductDto> magentoProducts){
        Set<CatalogProduct> products = new HashSet<CatalogProduct>();
        for (MagentoProductDto mProduct:magentoProducts){
            CatalogProduct product = new CatalogProduct();
            product.setName(mProduct.name);
            //TODO: setProductNumber to string
            product.setProductNumber(mProduct.sku);
            product.setProductType(ProductType.PRODUCT);
            product.setRecommendedPriceNet(new Amount(new BigDecimal(mProduct.regular_price_without_tax), Currency.EUR));
            product.setId(Long.valueOf(mProduct.entity_id));
            products.add(product);
        }
        return products;
    }
}
