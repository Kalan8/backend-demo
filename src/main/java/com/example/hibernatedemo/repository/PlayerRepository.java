package com.example.hibernatedemo.repository;

import com.example.hibernatedemo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
