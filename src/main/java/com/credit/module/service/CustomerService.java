package com.credit.module.service;

import com.credit.module.model.Customer;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    Customer getCustomerById(long id);

    ResponseEntity<Object> createCustomer(Customer customer);
}
