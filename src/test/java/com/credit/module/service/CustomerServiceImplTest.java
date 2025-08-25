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
    void getCustomerById_returnsCustomer_whenPresentUser() {
        Customer customer = new Customer();
        customer.setId(42L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setUserId("john42");
        when(userRepository.findByUserId("john42")).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByUserId("john42");

        assertNotNull(result);
        assertEquals(42L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        verify(userRepository).findByUserId("john42");
    }

    @Test
    void getCustomerByUserId_returnsNull_whenNotFound() {
        when(userRepository.findByUserId("userX")).thenReturn(Optional.empty());

        Customer result = customerService.getCustomerByUserId("userX");

        assertNull(result);
        verify(userRepository).findByUserId("userX");
    }

    @Test
    void createCustomer_savesAndReturnsOk() {
        Customer newCustomer = new Customer();
        newCustomer.setName("John");
        newCustomer.setSurname("Doe");
        newCustomer.setUserId("john42");
        newCustomer.setPassword("john42");
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
