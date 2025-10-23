package com.naruto.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naruto.system.domain.Mision;

@Repository
public interface MisionRepository extends JpaRepository<Mision, Long> {
}