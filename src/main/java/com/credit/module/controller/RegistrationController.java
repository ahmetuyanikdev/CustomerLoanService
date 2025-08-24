package com.credit.module.controller;

import com.credit.module.model.Customer;
import com.credit.module.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }
}
