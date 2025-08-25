package com.credit.module.controller;

import com.credit.module.data.LoanCreation;
import com.credit.module.data.LoanPayment;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import com.credit.module.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping(value = "/list/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #customerId == authentication.principal.username)")
    public List<Loan> listCustomerLoans(@PathVariable String customerId) {
        return loanService.listCustomerLoans(customerId);
    }

    @GetMapping(value = "/installments/{customerId}/{loanId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #customerId == authentication.principal.username)")
    public List<LoanInstallment> listLoanInstallments(@PathVariable String customerId, @PathVariable long loanId) {
        return loanService.listLoanInstallments(loanId);
    }

    @PostMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #customerId == authentication.principal.username)")
    public ResponseEntity<Object> save(@PathVariable String customerId, @RequestBody LoanCreation loanCreation) {
        return loanService.createCustomerLoan(loanCreation);
    }

    @PutMapping("/payment/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #customerId == authentication.principal.username)")
    public ResponseEntity<Object> payLoan(@PathVariable String customerId, @RequestBody LoanPayment loanPayment) {
        return loanService.payLoan(loanPayment);
    }
}
