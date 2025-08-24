package com.credit.module.validator;

import com.credit.module.dao.LoanInstallmentRepository;
import com.credit.module.data.LoanPayment;
import com.credit.module.model.Customer;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.SimpleErrors;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoanPaymentValidatorTest {
    private LoanInstallmentRepository loanInstallmentRepository;
    private LoanPaymentValidator loanPaymentValidator;

    @BeforeEach
    public void setUp() {
        loanInstallmentRepository = mock(LoanInstallmentRepository.class);
        loanPaymentValidator = new LoanPaymentValidator();
        loanPaymentValidator.setLoanInstallmentRepository(loanInstallmentRepository);
    }

    @Test
    public void validateObject_returnsErrors_whenAllLoanInstallmentsPaid() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setCreditLimit(10f);
        Loan loan = new Loan();
        loan.setCustomerId(1L);
        loan.setId(1L);
        loan.setLoanAmount(100f);
        loan.setInterestRate(0.2f);
        loan.setPaid(true);

        LoanInstallment loanInstallment1 = new LoanInstallment();
        loanInstallment1.setAmount(40f);
        loanInstallment1.setLoanId(1L);
        loanInstallment1.setPaid(true);

        LoanInstallment loanInstallment2 = new LoanInstallment();
        loanInstallment2.setAmount(40f);
        loanInstallment2.setLoanId(1L);
        loanInstallment2.setPaid(true);

        LoanInstallment loanInstallment3 = new LoanInstallment();
        loanInstallment3.setAmount(40f);
        loanInstallment3.setLoanId(1L);
        loanInstallment3.setPaid(true);
        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setAmount(130f);
        loanPayment.setLoanId(1L);

        when(loanInstallmentRepository.findAllByLoanId(anyLong())).thenReturn(List.of(loanInstallment1,
                loanInstallment2, loanInstallment3));

        SimpleErrors bindingResult = (SimpleErrors) loanPaymentValidator.validateObject(loanPayment);
        assertTrue(bindingResult.hasErrors());
        assertTrue(bindingResult.getAllErrors().stream().
                anyMatch(error -> Objects.equals(error.getCode(), "loan.payment.installments.paid")));
    }

    @Test
    public void validateObject_returnsNoErrors_whenLoanInstallmentsNotPaid() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setCreditLimit(10f);
        Loan loan = new Loan();
        loan.setCustomerId(1L);
        loan.setId(1L);
        loan.setLoanAmount(100f);
        loan.setInterestRate(0.2f);
        loan.setPaid(true);

        LoanInstallment loanInstallment1 = new LoanInstallment();
        loanInstallment1.setAmount(40f);
        loanInstallment1.setLoanId(1L);
        loanInstallment1.setPaid(false);
        loanInstallment1.setDueDate(LocalDate.now());

        LoanInstallment loanInstallment2 = new LoanInstallment();
        loanInstallment2.setAmount(40f);
        loanInstallment2.setLoanId(1L);
        loanInstallment2.setPaid(false);
        loanInstallment2.setDueDate(LocalDate.now().plusMonths(1));

        LoanInstallment loanInstallment3 = new LoanInstallment();
        loanInstallment3.setAmount(40f);
        loanInstallment3.setLoanId(1L);
        loanInstallment3.setPaid(false);
        loanInstallment3.setDueDate(LocalDate.now().plusMonths(2));

        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setAmount(130f);
        loanPayment.setLoanId(1L);

        when(loanInstallmentRepository.findAllByLoanId(anyLong())).thenReturn(List.of(loanInstallment1,
                loanInstallment2, loanInstallment3));

        SimpleErrors bindingResult = (SimpleErrors) loanPaymentValidator.validateObject(loanPayment);
        assertFalse(bindingResult.hasErrors());
    }
}
