package com.flexible.order.service.helper;

import java.util.ArrayList;
import java.util.List;

import com.flexible.order.web.dto.ItemDto;

public class ItemDtoFilterHelper {
	
	public static List<ItemDto> filterQtyLeftZero(List<ItemDto> itemDtos){
		List<ItemDto> returnedItemDto = new ArrayList<ItemDto>();
		for (ItemDto itemDto:itemDtos)
			if (itemDto.quantityLeft != 0)
				returnedItemDto.add(itemDto);
		return returnedItemDto;
	}

}
