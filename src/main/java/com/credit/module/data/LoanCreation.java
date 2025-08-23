package com.credit.module.data;

import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import lombok.Data;

import java.util.List;

@Data
public class LoanCreation {
    private Loan loan;
    private List<LoanInstallment> loanInstallments;
}
