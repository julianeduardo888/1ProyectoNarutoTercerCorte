package com.naruto.system.dto;

import java.util.List;

import com.naruto.system.domain.Mision;
import com.naruto.system.domain.Rango;
import com.naruto.system.domain.RangoMision;

// Usamos una clase normal aquí para poder modificar la lista de ninjas
public class MisionConNinjasDTO {
    private final Long id;
    private final String titulo;
    private final RangoMision rango;
    private final double recompensa;
    private final Rango rangoRequerido;
    private List<NinjaResumenDTO> ninjasElegibles; // La nueva lista

    // Constructor para convertir de Entidad a DTO
    public MisionConNinjasDTO(Mision mision) {
        this.id = mision.getId();
        this.titulo = mision.getTitulo();
        this.rango = mision.getRango();
        this.recompensa = mision.getRecompensa();
        this.rangoRequerido = mision.getRangoRequerido();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public RangoMision getRango() { return rango; }
    public double getRecompensa() { return recompensa; }
    public Rango getRangoRequerido() { return rangoRequerido; }
    public List<NinjaResumenDTO> getNinjasElegibles() { return ninjasElegibles; }
    public void setNinjasElegibles(List<NinjaResumenDTO> ninjasElegibles) {
        this.ninjasElegibles = ninjasElegibles;
    }
}