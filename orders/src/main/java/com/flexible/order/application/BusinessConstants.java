package com.flexible.order.application;

import com.flexible.order.domain.embeddable.Address;
import com.flexible.order.reference.Country;

public class BusinessConstants {

	public static final Address MY_ADDRESS = new Address(
			"Priebes", "OHG", "Maxstrasse 1", 71636, "Ludwigsburg", Country.DEUTSCHLAND
    );
}
