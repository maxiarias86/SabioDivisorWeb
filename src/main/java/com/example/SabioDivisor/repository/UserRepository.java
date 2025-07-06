package com.example.SabioDivisor.repository;

import com.example.SabioDivisor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public class UserRepository {

    @Repository
    public interface UserRepositoryInterface extends JpaRepository<User, Long> {
        List<User> findByName(String name);
    }
}
