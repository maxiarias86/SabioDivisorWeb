package com.example.SabioDivisor.dto;

import com.example.SabioDivisor.model.AppUser;

public class BalanceAmountDTO {
    private AppUser friend;
    private Double amount;

    public BalanceAmountDTO(AppUser friend, Double amount) {
        this.friend = friend;
        this.amount = amount;
    }

    public AppUser getFriend() {
        return friend;
    }

    public void setFriend(AppUser friend) {
        this.friend = friend;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
