package com.credit.module.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Customer extends User {

    @NotNull
    private float creditLimit;

    @NotNull
    private float usedCreditLimit;
}
