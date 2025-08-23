package com.credit.module.util;

import com.credit.module.model.LoanInstallment;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

public class LoanUtil {

    public static List<LoanInstallment> getEligibleToPayLoanInstallments(List<LoanInstallment> loanInstallmentList) {
        LocalDate currentDate = LocalDate.now();
        LocalDate fistDayOfFirstMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        LocalDate lastDayOfThirdMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth().plus(3),
                YearMonth.of(currentDate.getYear(), currentDate.getMonth().plus(3)).lengthOfMonth());

        return loanInstallmentList.stream().filter(loanInstallment -> {
            LocalDate dueDate = loanInstallment.getDueDate();
            return (dueDate.isEqual(fistDayOfFirstMonth) || dueDate.isAfter(fistDayOfFirstMonth)) &&
                    (dueDate.isEqual(lastDayOfThirdMonth) || dueDate.isBefore(lastDayOfThirdMonth));
        }).sorted(Comparator.comparing(LoanInstallment::getDueDate)).toList();
    }
}
