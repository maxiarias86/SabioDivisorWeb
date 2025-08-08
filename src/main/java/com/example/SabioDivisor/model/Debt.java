package com.example.SabioDivisor.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity//Significa que esta clase es una entidad JPA, lo que significa que se corresponde con una tabla (nombre por defecto: "debt") de la BBDD.
public class Debt {
    @Id//Indica que este campo es la clave primaria de la entidad.
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Le dice a JPA que genere el valor automáticamente
    private Long id;

    private double amount;

    @ManyToOne//Cada deuda tiene un solo AppUser como deudor. Un deudor puede tener muchas deudas.
    @JoinColumn(name = "debtor_id", nullable = false)//Indica que este campo es una clave foránea que referencia a la tabla AppUser. El nombre de la columna en esta tabla será "debtor_id". No puede ser nulo. Sería como un REFERENCES en SQL.
    private AppUser debtor;

    @ManyToOne
    @JoinColumn(name = "creditor_id", nullable = false)
    private AppUser creditor;

    @ManyToOne//Cada deuda está asociada a un solo gasto. Un gasto puede tener muchas deudas.
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    private LocalDate dueDate;
    private int installmentNumber;

    public Debt(){}//Todas las Entitys deben tener un constructor sin parámetros para que JPA pueda instanciarlas.

    public Debt(double amount, AppUser debtor, AppUser creditor, Expense expense, LocalDate dueDate, int installmentNumber) {
        setAmount(amount);
        setDebtor(debtor);
        setCreditor(creditor);
        setExpense(expense);
        setDueDate(dueDate);
        setInstallmentNumber(installmentNumber);
    }

    public Debt(Long id, double amount, AppUser debtor, AppUser creditor, Expense expense, LocalDate dueDate, int installmentNumber) {
        this(amount, debtor, creditor, expense, dueDate, installmentNumber);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AppUser getDebtor() {
        return debtor;
    }

    public void setDebtor(AppUser debtor) {
        this.debtor = debtor;
    }

    public AppUser getCreditor() {
        return creditor;
    }

    public void setCreditor(AppUser creditor) {
        this.creditor = creditor;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(int installmentNumber) {
        this.installmentNumber = installmentNumber;
    }
}