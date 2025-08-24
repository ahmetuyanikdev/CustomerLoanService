package com.credit.module.controller;

import com.credit.module.model.Customer;
import com.credit.module.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getCustomer(@PathVariable long customerId) {
        return customerService.getCustomerById(customerId);
    }
}
