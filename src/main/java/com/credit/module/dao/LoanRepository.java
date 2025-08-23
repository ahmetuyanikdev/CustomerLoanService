package com.credit.module.dao;

import com.credit.module.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByCustomerId(long customerId);
}
