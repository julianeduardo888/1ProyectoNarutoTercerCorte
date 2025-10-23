package com.naruto.system.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Mision {
    // Ya no implementa Visitable, el servicio de reportes se encargará

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;
    
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RangoMision rango;

    private double recompensa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rango rangoRequerido;

    // EL CAMPO 'completada' SE HA ELIMINADO
    
    // Constructor vacío para JPA
    public Mision() {}
    
    // ... (Tu constructor con parámetros)
    public Mision(String titulo, String descripcion, RangoMision rango, double recompensa, Rango rangoRequerido) {
        // ... (Tu lógica de validación)
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.rango = rango;
        this.recompensa = recompensa;
        this.rangoRequerido = rangoRequerido;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public RangoMision getRango() { return rango; }
    public void setRango(RangoMision rango) { this.rango = rango; }
    public double getRecompensa() { return recompensa; }
    public void setRecompensa(double recompensa) { this.recompensa = recompensa; }
    public Rango getRangoRequerido() { return rangoRequerido; }
    public void setRangoRequerido(Rango rangoRequerido) { this.rangoRequerido = rangoRequerido; }

    // Lógica movida al servicio
    // public boolean puedeTomarla(Ninja n){ ... }
}