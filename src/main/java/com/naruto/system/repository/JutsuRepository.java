package com.naruto.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naruto.system.domain.Jutsu;

@Repository
public interface JutsuRepository extends JpaRepository<Jutsu, Long> {
    // También buscará un jutsu por su nombre
    Optional<Jutsu> findByNombre(String nombre);
}  