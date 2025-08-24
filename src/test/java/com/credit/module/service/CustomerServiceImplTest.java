package com.credit.module.service;

import com.credit.module.dao.UserRepository;
import com.credit.module.model.Customer;
import com.credit.module.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {
    private UserRepository userRepository;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        customerService = new CustomerServiceImpl();
        customerService.setUserRepository(userRepository);
    }

    @Test
    void getCustomerById_returnsCustomer_whenPresent() {
        Customer customer = new Customer();
        customer.setId(42L);
        customer.setName("John");
        customer.setSurname("Doe");

        when(userRepository.findById(42L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(42L);

        assertNotNull(result);
        assertEquals(42L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        verify(userRepository).findById(42L);
    }

    @Test
    void getCustomerById_returnsNull_whenNotFound() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        Customer result = customerService.getCustomerById(100L);

        assertNull(result);
        verify(userRepository).findById(100L);
    }

    @Test
    void createCustomer_savesAndReturnsOk() {
        Customer newCustomer = new Customer();
        newCustomer.setName("John");
        newCustomer.setSurname("Doe");

        when(userRepository.save(any(Customer.class))).thenReturn(newCustomer);

        ResponseEntity<Object> response = customerService.createCustomer(newCustomer);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Customer saved successfully", response.getBody());

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(userRepository).save(captor.capture());
        Customer saved = captor.getValue();
        assertEquals("John", saved.getName());
        assertEquals("Doe", saved.getSurname());
    }

}
