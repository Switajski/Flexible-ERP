package com.flexible.order.service.conversion;

import org.springframework.stereotype.Service;

import com.flexible.order.domain.CatalogDeliveryMethod;
import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.domain.embeddable.DeliveryMethod;
import com.flexible.order.reference.Country;
import com.flexible.order.web.dto.DeliveryMethodDto;

@Service
public class DeliveryMethodDtoConverterService {

	public CatalogDeliveryMethod toDeliveryMethod(DeliveryMethodDto dto,
			CatalogDeliveryMethod catalogDeliveryMethod) {
	    DeliveryMethod dm = catalogDeliveryMethod.getDeliveryMethod();
			Address address = new Address(
					dto.getName1(),
					dto.getName2(),
					dto.getStreet(),
					dto.getPostalCode(),
					dto.getCity(),
					Country.DEUTSCHLAND);
			dm.setAddress(address);
			dm.setId(dto.getId());
			dm.setName(dto.getName());
			return catalogDeliveryMethod;
	}

}
