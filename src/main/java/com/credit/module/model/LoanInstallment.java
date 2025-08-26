package com.credit.module.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class LoanInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long loanId;

    @NotNull
    private float amount;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float paidAmount;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dueDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate paymentDate;

    @JsonProperty(value = "paid", access = JsonProperty.Access.READ_ONLY)
    private boolean isPaid;

    @JsonProperty(value = "overdue", access = JsonProperty.Access.READ_ONLY, defaultValue = "false")
    private boolean isOverdue;

    @JsonProperty(value = "unDue", access = JsonProperty.Access.READ_ONLY, defaultValue = "false")
    private boolean isUnDue;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float unDoOrOverdueCharge;
}
