package com.example.SabioDivisor.service;

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

    public List<String> getUserBalances(AppUser user, LocalDate date) {
        List<String> balances = new ArrayList<>();
        List<AppUser> friends = appUserRepository.findAll();

        for (AppUser friend : friends) {
            if (friend.getId().equals(user.getId())) {
                continue;// Saltea la iteración del usuario actual
            }
            List<Payment> paymentsBetween = paymentRepository.findByPayerOrRecipient(user.getId(), friend.getId(), date);
            List<Debt> debtsBetween = debtRepository.findByPayerOrRecipient(user.getId(), friend.getId(), date);

            Double balance = 0.0;

            for (Payment payment : paymentsBetween) {
                if (payment.getPayer().getId().equals(user.getId())) {
                    balance += payment.getAmount(); // Suma el pago al balance
                } else if (payment.getRecipient().getId().equals(user.getId())) {
                    balance -= payment.getAmount(); // Resta el pago al balance
                }
            }

            for (Debt debt : debtsBetween) {
                if (debt.getDebtor().getId().equals(user.getId())) {
                    balance -= debt.getAmount(); // Resta la deuda al balance
                } else if (debt.getCreditor().getId().equals(user.getId())) {
                    balance += debt.getAmount(); // Suma la deuda al balance
                }
            }

            if (balance != 0.0) {
                balances.add(String.format("ID %d - %s: $%.2f", friend.getId(), friend.getName(), balance));
            }
        }
        return balances;
    }
}
