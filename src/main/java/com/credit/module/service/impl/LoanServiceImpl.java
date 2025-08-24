package com.credit.module.service.impl;

import com.credit.module.dao.CustomerRepository;
import com.credit.module.dao.LoanInstallmentRepository;
import com.credit.module.dao.LoanRepository;
import com.credit.module.data.LoanCreation;
import com.credit.module.data.LoanPayment;
import com.credit.module.data.LoanPaymentResult;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import com.credit.module.service.LoanService;
import com.credit.module.util.LoanUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Setter
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanInstallmentRepository loanInstallmentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    @Qualifier("loanCreationValidator")
    private Validator loanCreationValidator;

    @Autowired
    @Qualifier("loanPaymentValidator")
    private Validator loanPaymentValidator;

    @Override
    public List<Loan> listCustomerLoans(long customerId) {
        return loanRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<LoanInstallment> listLoanInstallments(long loanId) {
        return loanInstallmentRepository.findAllByLoanId(loanId);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> createCustomerLoan(LoanCreation loanCreation) {
        Errors errors = loanCreationValidator.validateObject(loanCreation);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }
        LocalDate localDate = LocalDate.now();
        loanCreation.getLoan().setCreateDate(localDate);
        Loan loan = loanRepository.save(loanCreation.getLoan());
        int i = 1;
        setNextInstallmentDueDates(loanCreation, loan, localDate, i);
        loanInstallmentRepository.saveAll(loanCreation.getLoanInstallments());
        return ResponseEntity.ok().body("Loan and installments created successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<Object> payLoan(LoanPayment loanPayment) {
        Errors errors = loanPaymentValidator.validateObject(loanPayment);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }
        List<LoanInstallment> unPaidLoanInstallments =
                loanInstallmentRepository.findAllByLoanId(loanPayment.getLoanId()).stream().
                        filter(installment -> !installment.isPaid()).toList();

        List<LoanInstallment> loanInstallmentsToPay = LoanUtil.getEligibleToPayLoanInstallments(unPaidLoanInstallments);
        List<LoanInstallment> paidLoanInstallments = markAndGetPaidLoanInstallments(loanPayment, loanInstallmentsToPay);
        List<LoanInstallment> installmentPaymentResult = loanInstallmentRepository.saveAll(paidLoanInstallments);

        updateCustomerCreditAfterPayment(loanPayment);

        Optional<LoanInstallment> isThereNotPaidInstallment =
                loanInstallmentRepository.findAllByLoanId(loanPayment.getLoanId()).stream().
                        filter(installment -> !installment.isPaid()).findAny();

        if (isThereNotPaidInstallment.isEmpty()) {
            loanRepository.findById(loanPayment.getLoanId()).ifPresent(loan -> {
                loan.setPaid(true);
                loanRepository.save(loan);
            });
        }

        double totalAmountSpent = installmentPaymentResult.stream().mapToDouble(LoanInstallment::getPaidAmount).sum();

        LoanPaymentResult loanPaymentResult = LoanPaymentResult.builder().totalAmountSpent((float) totalAmountSpent).
                isLoanPaymentCompleted(isThereNotPaidInstallment.isEmpty()).
                numberOfPaidInstallments(installmentPaymentResult.size()).build();
        return ResponseEntity.ok().body(loanPaymentResult);
    }

    private void updateCustomerCreditAfterPayment(LoanPayment loanPayment) {
        Optional<Loan> loanOptional = loanRepository.findById(loanPayment.getLoanId());
        loanOptional.flatMap(
                        loan -> customerRepository.findById(loan.getCustomerId()))
                .ifPresent(customer -> {
                    customer.setUsedCreditLimit(customer.getUsedCreditLimit() + loanPayment.getAmount());
                    customerRepository.save(customer);
                });
    }

    private static List<LoanInstallment> markAndGetPaidLoanInstallments(LoanPayment loanPayment,
                                                                        List<LoanInstallment> loanInstallmentsToPay) {
        List<LoanInstallment> paidLoanInstallments = new LinkedList<>();
        loanInstallmentsToPay.forEach(installment -> {
            if (installment.getAmount() <= loanPayment.getAmount()) {
                installment.setPaid(true);
                installment.setPaymentDate(LocalDate.now());
                installment.setPaidAmount(installment.getAmount());
                loanPayment.setAmount(loanPayment.getAmount() - installment.getAmount());
                paidLoanInstallments.add(installment);
            }
        });
        return paidLoanInstallments;
    }

    private static void setNextInstallmentDueDates(LoanCreation loanCreation, Loan loan, LocalDate localDate, int i) {
        for (LoanInstallment installment : loanCreation.getLoanInstallments()) {
            installment.setLoanId(loan.getId());
            LocalDate firstDayOfNextMonth = localDate.withMonth(localDate.getMonthValue() + i).withDayOfMonth(1);
            installment.setDueDate(firstDayOfNextMonth);
            i++;
        }
    }
}
