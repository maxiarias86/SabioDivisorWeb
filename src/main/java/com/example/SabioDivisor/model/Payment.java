package com.example.SabioDivisor.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PAYMENT") // Valor que se guarda en la columna transaction_type
public class Payment extends Transaction {

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private AppUser payer;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private AppUser recipient;

    public Payment(){}

    public Payment(double amount, LocalDate date, AppUser payer, AppUser recipient) {
        super(0L, amount, date); // El ID lo asigna la base de datos
        setPayer(payer);
        setRecipient(recipient);
    }

    public Payment(long id, double amount, LocalDate date, AppUser payer, AppUser recipient) {
        super(id, amount, date); // El ID lo asigna la base de datos
        setPayer(payer);
        setRecipient(recipient);
    }

    public AppUser getPayer() {
        return payer;
    }

    public void setPayer(AppUser payer) {
        this.payer = payer;
    }

    public AppUser getRecipient() {
        return recipient;
    }

    public void setRecipient(AppUser recipient) {
        this.recipient = recipient;
    }
}
