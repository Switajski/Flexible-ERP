package com.flexible.order.web.helper;

import java.util.Collection;

import org.springframework.data.domain.Page;

import com.flexible.order.json.JsonObjectResponse;
import com.flexible.order.web.dto.ItemDto;

public class ExtJsResponseCreator {
	
	public static JsonObjectResponse createResponse(Page<ItemDto> reportItems) throws Exception{
		JsonObjectResponse response = new JsonObjectResponse();
		response.setData(reportItems.getContent());
		response.setTotal(reportItems.getTotalElements());
		response.setMessage("All entities retrieved.");
		response.setSuccess(true);
		return response;
	}

	public static JsonObjectResponse createFailedReponse(Exception e) {
		JsonObjectResponse response = new JsonObjectResponse();
		response.setData(e.getMessage());
		response.setMessage(e.getMessage());
		response.setSuccess(false);
		response.setTotal(0);
		return response;
	}

	public static JsonObjectResponse createResponse(Collection<Object> entities) {
		JsonObjectResponse response = new JsonObjectResponse();
		response.setData(entities);
		response.setMessage("Report items successfully handled");
		response.setSuccess(true);
		response.setTotal(entities.size());
		return response;
	}

	public static JsonObjectResponse createResponse(Object entity) {
		JsonObjectResponse response = new JsonObjectResponse();
		response.setData(entity);
		response.setMessage("Report items successfully handled");
		response.setSuccess(true);
		response.setTotal(1);
		return response;
	}

}
