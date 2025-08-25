package com.credit.module.service;

import com.credit.module.model.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {

    Customer getCustomerByUserId(String userId);

    ResponseEntity<Object> createCustomer(Customer customer);

    List<Customer> listAllCustomers();
}
