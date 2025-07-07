package com.example.SabioDivisor.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
    private AppUser debtor;

    @ManyToOne
    @JoinColumn(name = "creditor_id", nullable = false)
    private AppUser creditor;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    private LocalDate dueDate;
    private int installmentNumber;

    public Debt(){}

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