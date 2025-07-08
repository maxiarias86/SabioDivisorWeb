package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
