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

    public Debt save(Debt debt) {
        if(debt.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto de la deuda debe ser mayor que cero"); // Lanza excepciones que luego serán captadas en el controller.
        }
        if(debt.getDueDate() == null) {
            throw new IllegalArgumentException("Falta el dueDate del gasto");
        }
        if(debt.getCreditor() == null || debt.getDebtor() == null) {
            throw new IllegalArgumentException("Faltan los campos de acreedor o deudor");
        }
        if(debt.getDebtor().getId().equals(debt.getCreditor().getId())) {
            throw new IllegalArgumentException("El deudor y el acreedor no pueden ser la misma persona");
        }
        if(debt.getInstallmentNumber() <1 || debt.getInstallmentNumber() > 24) {
            throw new IllegalArgumentException("El número de cuota debe estar entre 1 y 24");
        }
        return debtRepository.save(debt);
    }

    public void delete(Long id) {
        debtRepository.deleteById(id);}

    public Debt findById(Long id) {return debtRepository.findById(id).orElse(null);}

    public List<Debt> findDebtsByExpenseId(Long expenseId) {
        if (expenseId == null) {
            throw new IllegalArgumentException("Falta el ID del gasto"); // Lanza una excepción si el ID del gasto es nulo
        }
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("El gasto con ID " + expenseId + " no existe"); // Lanza una excepción si el gasto no existe
        }

        return debtRepository.findAllByExpenseIdOrderByDueDateDesc(expenseId);
    }

    public List<Debt> findDebtsByExpenseIdAndUserId(Long userId, Long expenseId) {
        if (userId == null || expenseId == null) {
            throw new IllegalArgumentException("Faltan parámetros para buscar deudas"); // Lanza una excepción si falta algún parámetro
        }
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("El gasto con ID " + expenseId + " no existe"); // Lanza una excepción si el gasto no existe
        }

        return debtRepository.findByExpenseIdAndUserId(userId, expenseId);
    }

}
