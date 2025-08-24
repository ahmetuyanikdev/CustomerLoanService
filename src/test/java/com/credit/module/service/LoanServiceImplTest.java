package com.credit.module.service;

import com.credit.module.dao.CustomerRepository;
import com.credit.module.dao.LoanInstallmentRepository;
import com.credit.module.dao.LoanRepository;
import com.credit.module.data.LoanCreation;
import com.credit.module.data.LoanPayment;
import com.credit.module.data.LoanPaymentResult;
import com.credit.module.model.Customer;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import com.credit.module.service.impl.LoanServiceImpl;
import com.credit.module.validator.LoanCreationValidator;
import com.credit.module.validator.LoanPaymentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class LoanServiceImplTest {
    private LoanInstallmentRepository loanInstallmentRepository;
    private LoanRepository loanRepository;
    private LoanServiceImpl loanService;
    private Validator loanCreationValidator;
    private Validator loanPaymentValidator;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        loanInstallmentRepository = mock(LoanInstallmentRepository.class);
        loanRepository = mock(LoanRepository.class);
        customerRepository = mock(CustomerRepository.class);
        loanCreationValidator = mock(LoanCreationValidator.class);
        loanPaymentValidator = mock(LoanPaymentValidator.class);
        loanService = new LoanServiceImpl();
        loanService.setLoanInstallmentRepository(loanInstallmentRepository);
        loanService.setLoanRepository(loanRepository);
        loanService.setCustomerRepository(customerRepository);
        loanService.setLoanCreationValidator(loanCreationValidator);
        loanService.setLoanPaymentValidator(loanPaymentValidator);

    }

    @Test
    public void listCustomerLoans_returnsEmptyList_whenNoLoans() {
        when(loanRepository.findAllByCustomerId(anyLong())).thenReturn(Collections.emptyList());
        List<Loan> loanInstallmentList = loanService.listCustomerLoans(1L);
        assert loanInstallmentList.isEmpty();
        verify(loanRepository).findAllByCustomerId(1L);
    }

    @Test
    public void listCustomerLoans_returnsLoanList_whenPresent() {
        Loan loan = new Loan();
        loan.setId(1L);
        when(loanRepository.findAllByCustomerId(anyLong())).thenReturn(List.of(loan));
        List<Loan> loanInstallmentList = loanService.listCustomerLoans(1L);
        assert loanInstallmentList.size() == 1;
        verify(loanRepository).findAllByCustomerId(1L);
    }

    @Test
    public void listLoanInstallments_returnsEmptyList_whenNoInstallments() {
        when(loanInstallmentRepository.findAllByLoanId(anyLong())).thenReturn(Collections.emptyList());
        List<LoanInstallment> loanInstallmentList = loanService.listLoanInstallments(1L);
        assert loanInstallmentList.isEmpty();
        verify(loanInstallmentRepository).findAllByLoanId(1L);
    }

    @Test
    public void listLoanInstallments_returnsLoanInstallmentList_whenPresent() {
        LoanInstallment loanInstallment = new LoanInstallment();
        loanInstallment.setId(1L);
        when(loanInstallmentRepository.findAllByLoanId(anyLong())).thenReturn(List.of(loanInstallment));
        List<LoanInstallment> loanInstallmentList = loanService.listLoanInstallments(1L);
        assert loanInstallmentList.size() == 1;
    }

    @Test
    public void createCustomerLoan_savesAndReturnsOk_whenValidationPassed() {
        LoanCreation loanCreation = new LoanCreation();
        Loan loan = new Loan();
        loan.setLoanAmount(200f);
        loan.setId(1L);
        loanCreation.setLoan(loan);
        loanCreation.getLoan().setCustomerId(1L);
        LoanInstallment loanInstallment1 = new LoanInstallment();
        loanInstallment1.setAmount(100f);
        loanInstallment1.setLoanId(1L);
        LoanInstallment loanInstallment2 = new LoanInstallment();
        loanInstallment2.setAmount(100f);
        loanInstallment2.setLoanId(1L);
        List<LoanInstallment> loanInstallments = List.of(loanInstallment1, loanInstallment2);
        loanCreation.setLoanInstallments(loanInstallments);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(loanCreationValidator.validateObject(any())).thenReturn(result);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(loanInstallmentRepository.saveAll(anyList())).thenReturn(loanInstallments);
        ResponseEntity<Object> response = loanService.createCustomerLoan(loanCreation);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(loanInstallment1.getDueDate().getMonth(), LocalDate.now().getMonth().plus(1));
        assertEquals(loanInstallment2.getDueDate().getMonth(), LocalDate.now().getMonth().plus(2));
        assertEquals("Loan and installments created successfully", response.getBody());
    }

    @Test
    public void paysLoan_paysAndReturnsOk_whenValidationPassed() {
        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setLoanId(1L);
        loanPayment.setAmount(100f);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setCreditLimit(0f);
        Loan loan = new Loan();
        loan.setLoanAmount(200f);
        loan.setId(1L);
        loan.setPaid(false);
        loan.setCustomerId(1L);
        LoanInstallment loanInstallment1 = new LoanInstallment();
        loanInstallment1.setAmount(100f);
        loanInstallment1.setLoanId(1L);
        loanInstallment1.setPaid(false);
        loanInstallment1.setDueDate(LocalDate.now());
        LoanInstallment loanInstallment2 = new LoanInstallment();
        loanInstallment2.setAmount(100f);
        loanInstallment2.setLoanId(1L);
        loanInstallment2.setPaid(false);
        loanInstallment2.setDueDate(LocalDate.now().plusMonths(1));
        List<LoanInstallment> loanInstallments = List.of(loanInstallment1, loanInstallment2);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        when(loanPaymentValidator.validateObject(any())).thenReturn(result);
        when(loanInstallmentRepository.saveAll(loanInstallments)).thenReturn(loanInstallments);
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(loanInstallmentRepository.findAllByLoanId(anyLong())).thenReturn(loanInstallments);

        ResponseEntity<Object> response = loanService.payLoan(loanPayment);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(((LoanPaymentResult) response.getBody()).isLoanPaymentCompleted());
        assertEquals(100f, loanInstallment1.getPaidAmount());
        assertTrue(loanInstallment1.isPaid());
        assertEquals(0f, loanInstallment2.getPaidAmount());
        assertFalse(loanInstallment2.isPaid());
        assertFalse(loan.isPaid());
    }
}
