package com.deva.Govyana.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deva.Govyana.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
