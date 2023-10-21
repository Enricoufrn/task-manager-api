package com.example.taskmanagerapi.repositories;

import com.example.taskmanagerapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM public.users WHERE login = ?1 AND active = TRUE", nativeQuery = true)
    Optional<User> findByLogin(String login);
}
