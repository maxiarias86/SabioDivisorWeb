package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.repository.DebtRepository;
import com.example.SabioDivisor.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Debt> findAll() {return debtRepository.findAll();}

    public Debt save(Debt debt) {return debtRepository.save(debt);}

    public void delete(Long id) {
        debtRepository.deleteById(id);}

    public Debt findById(Long id) {return debtRepository.findById(id).orElse(null);}

    public List<Debt> findDebtsByExpenseId(Long expenseId) {
        if (expenseId == null) {
            throw new RuntimeException("Falta el ID del gasto"); // Lanza una excepción si el ID del gasto es nulo
        }
        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("El gasto con ID " + expenseId + " no existe"); // Lanza una excepción si el gasto no existe
        }

        return debtRepository.findAllByExpenseId(expenseId);
    }

}
