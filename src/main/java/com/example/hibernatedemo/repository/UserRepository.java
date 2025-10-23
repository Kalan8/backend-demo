package com.example.hibernatedemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hibernatedemo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
