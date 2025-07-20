package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.Payment;
import com.example.SabioDivisor.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public List<Payment> findAllByUserId(Long id) {
        return repository.findAllByUserId(id);
    }

    public Payment findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Payment save(Payment payment) {
        if (
                payment == null
                        || payment.getPayer() == null
                        || payment.getAmount() <= 0
                        || payment.getRecipient() == null
                        || payment.getDate() == null
        ) {
            throw new IllegalArgumentException("Datos del pago inválidos");
        }
        if (payment.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("El pago no puede ser futuro");
        }
        return repository.save(payment);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
