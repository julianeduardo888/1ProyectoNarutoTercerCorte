package com.naruto.system.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Ninja {
    // Ya no implementa Visitable

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rango rango;

    // Relación: Muchos Ninjas pueden pertenecer a una Aldea
    @ManyToOne(fetch = FetchType.EAGER) // Cargar la aldea siempre con el ninja
    @JoinColumn(name = "aldea_id", nullable = false)
    private Aldea aldea;

    // Incrustamos los stats
    @Embedded
    private Stats stats;

    // Relación: Muchos Ninjas pueden aprender Muchos Jutsus
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ninja_jutsus",
        joinColumns = @JoinColumn(name = "ninja_id"),
        inverseJoinColumns = @JoinColumn(name = "jutsu_id")
    )
    private Set<Jutsu> jutsus = new HashSet<>();

    // Constructor vacío para JPA
    public Ninja() {
        this.stats = new Stats();
    }
    
    // ... (Tu constructor con parámetros adaptado)
    public Ninja(String nombre, Rango rango, Aldea aldea, Stats stats) {
        this.nombre = nombre;
        this.rango = rango;
        this.aldea = aldea;
        this.stats = stats;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Rango getRango() { return rango; }
    public void setRango(Rango rango) { this.rango = rango; }
    public Aldea getAldea() { return aldea; }
    public void setAldea(Aldea aldea) { this.aldea = aldea; }
    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }
    public Set<Jutsu> getJutsus() { return jutsus; }
    public void setJutsus(Set<Jutsu> jutsus) { this.jutsus = jutsus; }

    // Método de ayuda
    public void aprenderJutsu(Jutsu j){ 
        if (j != null) this.jutsus.add(j); 
    }
}