package com.naruto.system.dto;

import com.naruto.system.domain.Ninja;
import com.naruto.system.domain.Rango;

// Un 'record' es una forma moderna y concisa de crear una clase DTO
public record NinjaResumenDTO(
    Long id,
    String nombre,
    Rango rango
) {
    // Constructor para convertir fácilmente de Entidad a DTO
    public NinjaResumenDTO(Ninja ninja) {
        this(ninja.getId(), ninja.getNombre(), ninja.getRango());
    }
}