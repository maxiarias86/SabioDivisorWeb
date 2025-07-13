package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

/*
        Key Default Methods:
            save(S entity): Saves a given entity.
            saveAll(Iterable<S> entities): Saves all given entities.
            findById(ID id): Retrieves an entity by its ID. Returns an Optional to handle cases where the entity might not be found.
            existsById(ID id): Returns whether an entity with the given ID exists.
            findAll(): Returns all instances of the type.
            findAllById(Iterable<ID> ids): Returns all instances of the type with the given IDs.
            count(): Returns the number of entities available.
            delete(T entity): Deletes a given entity.
            deleteById(ID id): Deletes the entity with the given ID.
            deleteAll(): Deletes all entities managed by the repository.
            deleteAll(Iterable<? extends T> entities): Deletes the given entities.
            deleteAllInBatch(): Deletes all entities in a batch call.
            deleteAllByIdInBatch(Iterable<ID> ids): Deletes the entities identified by the given IDs using a single query.
            flush(): Flushes all pending changes to the database (specific to JpaRepository).
            saveAndFlush(S entity): Saves an entity and flushes changes immediately (specific to JpaRepository).
         */
}

