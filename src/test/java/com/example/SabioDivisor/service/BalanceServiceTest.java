package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Payment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BalanceServiceTest {

    @Test
    public void testCalculateBalance() {
        // Prueba unitaria para verificar el cálculo del balance entre pagos y deudas
        AppUser user = new AppUser(); user.setId(1L);// Asignamos un Long al ID al usuario
        AppUser friend = new AppUser(); friend.setId(2L);

        Payment payment = new Payment(100.0, LocalDate.now(), user, friend);// Pago de 100.0 del usuario a un amigo
        Debt debt = new Debt(50.0, user, friend, null, LocalDate.now(), 1);// Deuda de 50.0 del usuario hacia el amigo

        BalanceService balanceService = new BalanceService();
        double resultado = balanceService.calculateBalance(List.of(payment),List.of(debt),user);// Calcula el balance del usuario

        assertEquals(50.0, resultado, 0.001); // Compara el resultado esperado con el obtenido
    }

    @Test
    public void testCalculateBalanceExceptionWhenUserNull() {
        AppUser user = new AppUser(); // Al no setear un id el user sera un null
        AppUser friend = new AppUser(); friend.setId(2L);

        Payment payment = new Payment(100.0, LocalDate.now(), user, friend);
        Debt debt = new Debt(50.0, user, friend, null, LocalDate.now(), 1);

        BalanceService balanceService = new BalanceService();

        assertThrows(NullPointerException.class, () -> {
            balanceService.calculateBalance(List.of(payment), List.of(debt), user);
        });
    }
}
