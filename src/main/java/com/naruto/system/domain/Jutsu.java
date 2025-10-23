package com.naruto.system.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Jutsu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JutsuTipo tipo;
    
    // Constructor vacío requerido por JPA
    public Jutsu() {}

    // Constructor útil
    public Jutsu(String nombre, JutsuTipo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public JutsuTipo getTipo() { return tipo; }
    public void setTipo(JutsuTipo tipo) { this.tipo = tipo; }

    @Override 
    public String toString(){ 
        return nombre + " (" + tipo + ")"; 
    }
}