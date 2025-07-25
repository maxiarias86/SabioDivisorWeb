package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.payer.id = :id OR p.recipient.id = :id")
    List<Payment> findAllByUserId(@Param("id") Long id);

    @Query(
            "SELECT p FROM Payment p " +
            "WHERE ((p.payer.id = :payerId AND p.recipient.id = :recipientId) " +
            "OR (p.payer.id = :recipientId AND p.recipient.id = :payerId))"+
                    "AND p.date <= :date ORDER BY p.date DESC" // Filtra pagos hasta la fecha actual
    )
    List<Payment> findByPayerOrRecipient(@Param("payerId") Long payerId, @Param("recipientId") Long recipientId, @Param("date") LocalDate date);

}
