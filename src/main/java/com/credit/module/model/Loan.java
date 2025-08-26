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
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String customerId;

    @NotNull
    private float loanAmount;

    @NotNull
    private float interestRate;

    @NotNull
    private int numberOfInstallment;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private LocalDate createDate;

    @JsonProperty(value = "paid", access = JsonProperty.Access.READ_ONLY)
    private boolean isPaid;
}
