package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.AppUser;
import com.example.SabioDivisor.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(
            "SELECT e FROM Expense e WHERE e.id IN(" +
                    "SELECT d.expense.id FROM Debt d WHERE d.debtor.id = :userId OR d.creditor.id = :userId ORDER BY e.date DESC)"
    )
    List<Expense> findAllByUserId(@Param("userId") Long userId);
}

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
