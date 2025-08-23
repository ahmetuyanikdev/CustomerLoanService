package com.credit.module.validator;

import com.credit.module.dao.CustomerRepository;
import com.credit.module.data.LoanCreation;
import com.credit.module.model.Customer;
import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

@Component("loanCreationValidator")
public class LoanCreationValidator implements Validator {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoanCreation.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoanCreation loanCreation = (LoanCreation) target;
        Optional<Customer> customerOptional = customerRepository.findById(loanCreation.getLoan().getCustomerId());
        if (customerOptional.isEmpty()) {
            errors.reject("customer.not.found", "Customer not found");
            return;
        }
        Loan loan = loanCreation.getLoan();
        Customer customer = customerOptional.get();
        if (customer.getCreditLimit() - customer.getUsedCreditLimit() < loan.getLoanAmount()) {
            errors.reject("customer.credit.limit.not.sufficient", "Customer credit limit not sufficient");
            return;
        }
        if (!(loan.getNumberOfInstallment() % 3 == 0 && loan.getNumberOfInstallment() <= 24)) {
            errors.reject("customer.loan.number.of.installment.not.valid", "Loan number of installment not valid");
            return;
        }
        if (!(0.1 <= loan.getInterestRate() && loan.getInterestRate() <= 0.5)) {
            errors.reject("customer.loan.interest.rate.not.valid", "Loan interest rate not valid");
            return;
        }
        List<LoanInstallment> loanInstallment = loanCreation.getLoanInstallments();
        boolean allInstallmentAmountSame = loanInstallment.stream().map(LoanInstallment::getAmount).
                map(Float::intValue).distinct().count() == 1;
        if (!allInstallmentAmountSame) {
            errors.reject("customer.loan.installment.amounts.not.same", "Loan installment amounts not same");
            return;
        }
        float installmentsSum =
                (float) loanInstallment.stream().map(LoanInstallment::getAmount).mapToDouble(Float::doubleValue).sum();
        if (installmentsSum != loan.getLoanAmount() * (1 + loan.getInterestRate())) {
            errors.reject("customer.loan.installment.amounts.not.equal.to.loan.amount", "Loan installment amounts not" +
                    " equal to loan amount");
        }
    }
}
