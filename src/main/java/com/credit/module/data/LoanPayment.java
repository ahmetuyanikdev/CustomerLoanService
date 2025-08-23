package com.credit.module.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoanPayment {
    @JsonProperty(required = true)
    private long loanId;

    @JsonProperty(required = true)
    private float amount;
}
