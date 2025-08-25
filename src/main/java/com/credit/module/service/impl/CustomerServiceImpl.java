package com.credit.module.service.impl;

import com.credit.module.dao.UserRepository;
import com.credit.module.model.Customer;
import com.credit.module.service.CustomerService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter
public class CustomerServiceImpl implements CustomerService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Override
    public Customer getCustomerByUserId(String userId) {
        return (Customer) userRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public ResponseEntity<Object> createCustomer(Customer customer) {
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        userRepository.save(customer);
        return ResponseEntity.ok().body("Customer saved successfully");
    }

    @Override
    public List<Customer> listAllCustomers() {
        return userRepository.findAll().stream().
                filter(user -> user instanceof Customer).
                map(user -> (Customer) user).toList();
    }
}
