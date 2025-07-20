package com.example.SabioDivisor.dto;


import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Payment;

import java.util.List;

public class BalanceDTO {
    private AppUser friend;
    private Double balance;
    private List<Debt> debts;
    private List<Payment> payments;

    public BalanceDTO(AppUser friend, Double balance, List<Debt> debts, List<Payment> payments) {
        this.friend = friend;
        this.balance = balance;
        this.debts = debts;
        this.payments = payments;
    }

    public AppUser getFriend() {
        return friend;
    }

    public void setFriend(AppUser friend) {
        this.friend = friend;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
