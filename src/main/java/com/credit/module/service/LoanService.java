package com.credit.module.service;

import com.credit.module.data.LoanCreation;
import com.credit.module.data.LoanPayment;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {
    /**
     * list loans for the given customer
     *
     * @param customerId
     * @return
     */
    List<Loan> listCustomerLoans(long customerId);

    /**
     * list installments for the given loan
     *
     * @param loanId
     * @return
     */
    List<LoanInstallment> listLoanInstallments(long loanId);

    /**
     * create customer loan and installments
     *
     * @param loan
     */
    ResponseEntity<Object> createCustomerLoan(LoanCreation loan);

    /**
     * pay loan for a given loan id and amount
     *
     * @param loanPayment
     * @return
     */
    ResponseEntity<Object> payLoan(LoanPayment loanPayment);
}
