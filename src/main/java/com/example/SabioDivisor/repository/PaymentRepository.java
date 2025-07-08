package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.payer.id = :id OR p.recipient.id = :id")
    List<Payment> findAllByUserId(@Param("id") Long id);


}
