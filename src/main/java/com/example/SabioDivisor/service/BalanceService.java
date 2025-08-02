package com.example.SabioDivisor.service;

import com.example.SabioDivisor.dto.BalanceAmountDTO;
import com.example.SabioDivisor.dto.BalanceDTO;
import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Payment;
import com.example.SabioDivisor.repository.AppUserRepository;
import com.example.SabioDivisor.repository.DebtRepository;
import com.example.SabioDivisor.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DebtRepository debtRepository;

    public BalanceService() {}

    public double getBalance(List<BalanceAmountDTO> balances) {
        if (balances == null) {
            throw new IllegalArgumentException("Faltan los balances");
        }
        double balance = 0.0;
        for (BalanceAmountDTO balanceAmountDTO : balances) {
            if (balanceAmountDTO != null && balanceAmountDTO.getAmount() != null) {
                balance += balanceAmountDTO.getAmount();
            }
        }
        return balance;
    }

    public List<BalanceAmountDTO> getUserBalances(AppUser user, LocalDate date) {

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        List<BalanceAmountDTO> balances = new ArrayList<>();
        List<AppUser> friends = appUserRepository.findAll();

        for (AppUser friend : friends) {
            if (friend.getId().equals(user.getId())) {
                continue;// Saltea la iteración del usuario logueado.
            }

            List<Payment> paymentsBetween = this.findPaymentsBetweenUsers(user, friend, date);
            List<Debt> debtsBetween = this.findDebtsBetweenUsers(user, friend, date);

            Double balance = calculateBalance(paymentsBetween, debtsBetween, user);

            if (Math.abs(balance) >= 0.01) { // Al haber muchas cuotas y pagos se crean errores de redondeo por lo que si se deja solo distinto a 0, se mostrarían balances de 0.001 o -0.001 que no son significativos.
                balances.add(new BalanceAmountDTO(friend, balance));
            }
        }
        return balances;
    }

    public List<Debt> findDebtsBetweenUsers(AppUser user, AppUser friend, LocalDate date) {
        return debtRepository.findByPayerOrRecipient(user.getId(), friend.getId(), date);
    }

    public List<Payment> findPaymentsBetweenUsers(AppUser user, AppUser friend, LocalDate date) {
        return paymentRepository.findByPayerOrRecipient(user.getId(), friend.getId(), date);
    }
//Este metodo podría ser privado, ya que solo se usa dentro de la clase BalanceService pero para permitir su uso en las pruebas unitarias se deja como público.
    public Double calculateBalance (List<Payment> payments, List<Debt> debts, AppUser user) {
        Double balance = 0.0;

        for (Payment payment : payments) {
            if (payment.getPayer().getId().equals(user.getId())) {
                balance += payment.getAmount(); // Suma el pago al balance
            } else if (payment.getRecipient().getId().equals(user.getId())) {
                balance -= payment.getAmount(); // Resta el pago al balance
            }
        }

        for (Debt debt : debts) {
            if (debt.getDebtor().getId().equals(user.getId())) {
                balance -= debt.getAmount(); // Resta la deuda al balance
            } else if (debt.getCreditor().getId().equals(user.getId())) {
                balance += debt.getAmount(); // Suma la deuda al balance
            }
        }

        return balance;
    }

    public BalanceDTO getBalanceDTO(AppUser user, AppUser friend, LocalDate date) {
        if( user == null || friend == null || date == null) {
            throw new IllegalArgumentException("El usuario, amigo o fecha no pueden ser nulos");
        }
        List<Payment> paymentsBetween = this.findPaymentsBetweenUsers(user, friend, date);
        List<Debt> debtsBetween = this.findDebtsBetweenUsers(user, friend, date);

        Double balance = calculateBalance(paymentsBetween, debtsBetween, user);

        return new BalanceDTO(friend, balance, debtsBetween, paymentsBetween);
    }

}
