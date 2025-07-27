package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    @Modifying//Indica que este metodo modifica la base de datos, por lo que se debe usar dentro de una transacción.
    @Transactional//Indica que este metodo debe ejecutarse dentro de una transacción. Si algo falla, se revertirá lo que se haya hecho en la transacción.
    @Query(
            "DELETE FROM Debt d WHERE d.expense.id = :expenseId"
    )
    void deleteByExpenseId(Long expenseId);//Elimina todas las deudas asociadas a un gasto. Se usa en ExpenseService para eliminar las deudas al eliminar o editar un gasto.

    @Query(
            "SELECT COUNT(d) FROM Debt d WHERE d.expense.id = :expenseId AND (d.debtor.id = :userId OR d.creditor.id = :userId)"
    )
    Long countByExpenseId(@Param("expenseId") Long expenseId,@Param("userId") Long userId);//cuenta si el usuario participó en al menos una deuda asociada al gasto.

    List<Debt> findAllByExpenseId(Long expenseId);

    @Query(
            "SELECT d FROM Debt d " +
                    "WHERE ((d.debtor.id = :userId AND d.creditor.id = :friendId) OR (d.debtor.id = :friendId AND d.creditor.id = :userId)) " +
                    "AND d.dueDate <= :date ORDER BY d.dueDate DESC"
    )
    List<Debt> findByPayerOrRecipient(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("date") LocalDate date);


}

