package com.naruto.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naruto.system.domain.Ninja;

@Repository
public interface NinjaRepository extends JpaRepository<Ninja, Long> {
    // Las operaciones básicas como save(), findById(), findAll() ya vienen incluidas
}