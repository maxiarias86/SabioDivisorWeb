package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.Payment;
import com.example.SabioDivisor.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//AGREGAR VALIDACIONES PARA QUE NO SE PUEDA GUARDAR UN PAGO NULO. AUNQUE YA SE HAYA HECHO EN EL CONTROLLER
    public Payment save(Payment payment) {
        return repository.save(payment);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
