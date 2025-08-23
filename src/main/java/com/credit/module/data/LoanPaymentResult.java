package com.credit.module.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanPaymentResult {

    boolean isLoanPaymentCompleted;

    int numberOfPaidInstallments;

    float totalAmountSpent;
}
