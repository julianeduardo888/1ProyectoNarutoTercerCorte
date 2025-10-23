package com.naruto.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naruto.system.domain.AsignacionMision;

@Repository
public interface AsignacionMisionRepository extends JpaRepository<AsignacionMision, Long> {
}