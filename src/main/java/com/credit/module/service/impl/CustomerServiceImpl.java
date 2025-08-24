package com.credit.module.service.impl;

import com.credit.module.dao.CustomerRepository;
import com.credit.module.model.Customer;
import com.credit.module.service.CustomerService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Setter
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer getCustomerById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<Object> createCustomer(Customer customer) {
        customerRepository.save(customer);
        return ResponseEntity.ok().body("Customer saved successfully");
    }
}
