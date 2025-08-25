package com.credit.module.validator;

import com.credit.module.dao.UserRepository;
import com.credit.module.data.LoanCreation;
import com.credit.module.model.Customer;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.SimpleErrors;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoanCreationValidatorTest {
    private UserRepository userRepository;
    private LoanCreationValidator loanCreationValidator;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        loanCreationValidator = new LoanCreationValidator();
        loanCreationValidator.setUserRepository(userRepository);
    }

    @Test
    public void validateObject_returnsErrors_whenCustomerCreditLimitNotSufficient() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setCreditLimit(10f);
        Loan loan = new Loan();
        loan.setCustomerId("1L");
        loan.setId(1L);
        loan.setLoanAmount(100f);
        loan.setInterestRate(0.5f);
        loan.setPaid(false);
        LoanCreation loanCreation = new LoanCreation();
        loanCreation.setLoan(loan);

        LoanInstallment loanInstallment1 = new LoanInstallment();
        loanInstallment1.setAmount(75f);
        loanInstallment1.setLoanId(1L);
        loanInstallment1.setPaid(false);

        LoanInstallment loanInstallment2 = new LoanInstallment();
        loanInstallment2.setAmount(75f);
        loanInstallment2.setLoanId(1L);
        loanInstallment2.setPaid(false);
        when(userRepository.findByUserId("1L")).thenReturn(java.util.Optional.of(customer));
        loanCreation.setLoanInstallments(List.of(loanInstallment1, loanInstallment2));
        SimpleErrors bindingResult = (SimpleErrors) loanCreationValidator.validateObject(loanCreation);
        assertTrue(bindingResult.hasErrors());
        assertTrue(bindingResult.getAllErrors().stream().
                anyMatch(error -> Objects.equals(error.getCode(), "customer.credit.limit.not.sufficient")));
    }

    @Test
    public void validateObject_returnsNoErrors_whenCustomerCreditLimitSufficient() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setCreditLimit(160f);
        Loan loan = new Loan();
        loan.setCustomerId("1L");
        loan.setId(1L);
        loan.setLoanAmount(100f);
        loan.setInterestRate(0.5f);
        loan.setPaid(false);
        loan.setNumberOfInstallment(3);
        LoanCreation loanCreation = new LoanCreation();
        loanCreation.setLoan(loan);

        LoanInstallment loanInstallment1 = new LoanInstallment();
        loanInstallment1.setAmount(50f);
        loanInstallment1.setLoanId(1L);
        loanInstallment1.setPaid(false);

        LoanInstallment loanInstallment2 = new LoanInstallment();
        loanInstallment2.setAmount(50f);
        loanInstallment2.setLoanId(1L);
        loanInstallment2.setPaid(false);

        LoanInstallment loanInstallment3 = new LoanInstallment();
        loanInstallment3.setAmount(50f);
        loanInstallment3.setLoanId(1L);
        loanInstallment3.setPaid(false);

        when(userRepository.findByUserId("1L")).thenReturn(java.util.Optional.of(customer));
        loanCreation.setLoanInstallments(List.of(loanInstallment1, loanInstallment2, loanInstallment3));
        SimpleErrors bindingResult = (SimpleErrors) loanCreationValidator.validateObject(loanCreation);
        assertFalse(bindingResult.hasErrors());
    }
}
