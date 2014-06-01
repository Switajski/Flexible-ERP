package de.switajski.priebes.flexibleorders.web;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.service.CustomerDtoConverterServiceImpl;
import de.switajski.priebes.flexibleorders.web.dto.CustomerDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@RequestMapping("/customers")
@Controller
public class CustomerController extends ExceptionController{

	private CustomerRepository customerRepo;

//	private static Logger log = Logger.getLogger(CustomerController.class);
	
	@Autowired
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepo = customerRepository;
	}
	
	@RequestMapping(value = "/json", method=RequestMethod.GET)
	public @ResponseBody JsonObjectResponse listAll(@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "sort", required = false) String sorts){
		Page<Customer> customers = customerRepo.findAll(new PageRequest(page-1, limit));
		JsonObjectResponse response = ExtJsResponseCreator.createResponse(
				JsonSerializationHelper.convertToJsonCustomers(customers.getContent()));
		response.setTotal(customers.getTotalElements());
		return response;
	}
	
	//TODO: move to ReportController
	@RequestMapping(value = "listitems", produces = "text/html")
    public String confirm(Model uiModel) {
        return "customers/listitems";
    }
	
	//TODO let a serializer read and map these attribute
	@RequestMapping(value = "/create", method=RequestMethod.POST)
	public @ResponseBody JsonObjectResponse create(@RequestBody CustomerDto cDto
			) throws JsonParseException, JsonMappingException, IOException{
		Customer c = CustomerDtoConverterServiceImpl.toCustomer(cDto, new Customer());
		customerRepo.save(c);
		return ExtJsResponseCreator.createResponse(c);
	}

	public CustomerDto serializeJsonCustomer(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig();
		CustomerDto jsonCustomer = (CustomerDto) mapper.readValue(json, CustomerDto.class);
		return jsonCustomer;
	}
}
