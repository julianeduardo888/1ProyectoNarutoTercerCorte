package com.naruto.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naruto.system.domain.Aldea;

@Repository
public interface AldeaRepository extends JpaRepository<Aldea, Long> {
    // Spring creará mágicamente una consulta para buscar una aldea por su nombre
    Optional<Aldea> findByNombre(String nombre);
}