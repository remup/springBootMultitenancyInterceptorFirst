package com.example.datasource.first;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepo customerRepository;
	
	@Transactional
	public List<Customer> getCutomers() {
		// TODO Auto-generated method stub
		 List<Customer> customers = new ArrayList<Customer>();
		 customerRepository.findAll().forEach(customers::add);
		return customers;
	}

}
