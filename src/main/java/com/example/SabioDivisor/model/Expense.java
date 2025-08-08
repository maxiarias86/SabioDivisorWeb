package com.example.SabioDivisor.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("EXPENSE") // Valor que se guarda en transaction_type
public class Expense extends Transaction {
    private int installments;
    private String description;

    /*
    La relación entre Expense y Debt es unidireccional desde Debt hacia Expense.
    No uso la anotación @OneToMany en Expense, por lo tanto no hay borrado en cascada automático.
    En su lugar, manejo la eliminación manualmente en el ExpenseService, eliminando primero todas las Debt asociadas al Expense antes de eliminar el gasto.
     */

    public Expense() {}

    public Expense(double amount, int installments, String description) {
        super(0L, amount, null); // El ID lo asigna la base de datos
        setInstallments(installments);
        setDescription(description);
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}