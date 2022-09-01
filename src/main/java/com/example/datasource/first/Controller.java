package com.example.datasource.first;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@Autowired
	private CustomerService service;

	@RequestMapping(value = "/getData/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Customer>> getData(@PathVariable("id") String id)

	{

		CurrentTenant.setCurrentTenant(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("DataPrsent", "yes");
		return ResponseEntity.ok().headers(headers).body(service.getCutomers());
	}
}
