package com.credit.module.data;

import com.credit.module.model.Loan;
import com.credit.module.model.LoanInstallment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LoanCreation {
    private Loan loan;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<LoanInstallment> loanInstallments;
}
