package com.example.user.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
