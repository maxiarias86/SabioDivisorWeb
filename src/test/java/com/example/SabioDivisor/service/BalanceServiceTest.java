package com.example.SabioDivisor.service;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Debt;
import com.example.SabioDivisor.model.Payment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BalanceServiceTest {//Es una clase de prueba para BalanceService, que contiene métodos para probar la lógica.

    /*
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
     */

    @Test
    public void testCalculateBalance() {//Simula la situación normal con datos válidos.
        // Prueba unitaria para verificar el cálculo del balance entre pagos y deudas
        AppUser user = new AppUser();
        AppUser friend = new AppUser();
        user.setId(1L);// Asignamos un Long al ID al usuario
        friend.setId(2L);

        // Se crea un pago y una deuda entre el usuario y su amigo para que pueda hacerse un balance.
        Payment payment = new Payment(100.0, LocalDate.now(), user, friend);// Pago de 100.0 del usuario a un amigo
        Debt debt = new Debt(50.0, user, friend, null, LocalDate.now(), 1);// Deuda de 50.0 del usuario hacia el amigo

        BalanceService balanceService = new BalanceService();
        double resultado = balanceService.calculateBalance(List.of(payment),List.of(debt),user);// Utilizamos el BalanceService que calcula el balance del usuario
        assertEquals(50.0, resultado, 0.001); // Compara el resultado esperado con el obtenido
        //assertEquals es un metodo de JUnit que compara dos valores (esperado y observado) y lanza una excepción si no son iguales. Se puede agregar un tercer parámetro que es el margen de error, en este caso 0.001, para evitar errores de redondeo.
    }

    /*
    calculateBalance tiene una línea que verifica si el ID del pagador del pago es igual al ID del usuario que se pasa como parámetro.
    if (payment.getPayer().getId().equals(user.getId())) {
    Si el user.getId() es null, se lanzará una NullPointerException al intentar usar el metodo getId().
     */
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
