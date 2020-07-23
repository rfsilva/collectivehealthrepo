package com.collectivehealth.pollmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectivehealth.pollmanagement.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.username = :username and u.password = :password")
    Optional<User> findByUsernamePassword(@Param("username") String username, @Param("password") String password);

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
