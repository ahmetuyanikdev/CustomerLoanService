package com.credit.module.controller;

import com.credit.module.data.LoanCreation;
import com.credit.module.data.LoanPayment;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import com.credit.module.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Loan> listCustomerLoans(@PathVariable long customerId) {
        return loanService.listCustomerLoans(customerId);
    }

    @GetMapping(value = "/installments/{loanId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LoanInstallment> listLoanInstallments(@PathVariable long loanId) {
        return loanService.listLoanInstallments(loanId);
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody LoanCreation loanCreation) {
        return loanService.createCustomerLoan(loanCreation);
    }

    @PutMapping("/payment")
    public ResponseEntity<Object> payLoan(@RequestBody LoanPayment loanPayment) {
        return loanService.payLoan(loanPayment);
    }
}
