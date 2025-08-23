package com.credit.module.service;

import com.credit.module.model.Customer;

public interface CustomerService {

    Customer getCustomerById(long id);

    void createCustomer(Customer customer);
}
