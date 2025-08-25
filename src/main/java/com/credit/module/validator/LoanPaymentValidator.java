package com.credit.module.validator;

import com.credit.module.dao.LoanInstallmentRepository;
import com.credit.module.data.LoanPayment;
import com.credit.module.model.LoanInstallment;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

import static com.credit.module.util.LoanUtil.getEligibleToPayLoanInstallments;

/**
 * Validator implementation for validating the loan payment process. This class ensures
 * that the loan payments adhere to specific business constraints.
 *
 * This validator:
 * 1. Validates that the payment amount is greater than zero.
 * 2. Checks if there are any unpaid installments for the loan. If all installments
 *    have been paid, an error is reported.
 * 3. Ensures that the payment date falls within the valid range of the first day of
 *    the first month up to the last day of the third month for eligible installments.
 */

@Component(value = "loanPaymentValidator")
@Setter
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
