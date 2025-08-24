package com.credit.module.validator;


import com.credit.module.dao.LoanInstallmentRepository;
import com.credit.module.data.LoanPayment;
import com.credit.module.model.LoanInstallment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

import static com.credit.module.util.LoanUtil.getEligibleToPayLoanInstallments;

@Component(value = "loanPaymentValidator")
public class LoanPaymentValidator implements Validator {

    @Autowired
    private LoanInstallmentRepository loanInstallmentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoanPayment.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoanPayment loanPayment = (LoanPayment) target;
        if (loanPayment.getAmount() <= 0) {
            errors.reject("loan.payment.amount.invalid", "Loan payment amount must be greater than zero");
            return;
        }
        List<LoanInstallment> unpaidLoanInstallmentList =
                loanInstallmentRepository.findAllByLoanId(loanPayment.getLoanId()).stream()
                        .filter(loanInstallment -> !loanInstallment.isPaid()).toList();

        if (unpaidLoanInstallmentList.isEmpty()) {
            errors.reject("loan.payment.installments.paid", "All Loan installments have been paid already");
            return;
        }
        List<LoanInstallment> eligibleLoanInstallmentsToPay =
                getEligibleToPayLoanInstallments(unpaidLoanInstallmentList);

        if (eligibleLoanInstallmentsToPay.isEmpty()) {
            errors.reject("loan.payment.date.invalid", "Loan payment date must be between first day of first month " +
                    "and last day of third month");
        }
    }

}
