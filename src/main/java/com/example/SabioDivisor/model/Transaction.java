package com.example.SabioDivisor.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)//Estrategia JOINED: crea tabla para Transaction y una por cada subclase, unidas por ID.
@DiscriminatorColumn(name = "transaction_type")//Columna que indica el tipo de transacción (Payment o Expense).

public abstract class Transaction {
    @Id
    @GeneratedValue
    protected Long id;
    protected double amount;
    protected LocalDate date;

    public Transaction() {
    }
    public Transaction(Long id, double amount, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }

}