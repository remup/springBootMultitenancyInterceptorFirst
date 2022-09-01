package com.example.datasource.first;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="Customer")
public class Customer {
	@Id
	@Column(name="customer_id")
	private int customerId;
	@Column(name="customer_name")
	private String customerName;
	
	
	public Customer(int customerId, String customerName) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
	}
	
	public Customer() {
		
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	

	public String getCustomerName() {
		return customerName.trim();
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
