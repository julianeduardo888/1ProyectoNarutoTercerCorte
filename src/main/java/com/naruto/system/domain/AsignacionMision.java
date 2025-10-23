package com.naruto.system.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asignacion_mision")
public class AsignacionMision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ninja_id", nullable = false)
    private Ninja ninja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mision_id", nullable = false)
    private Mision mision;

    @Column(nullable = false)
    private boolean completada = false;

    private LocalDate fechaAsignacion;
    private LocalDate fechaCompletacion;
    
    // Constructor vacío para JPA
    public AsignacionMision() {}

    // Constructor útil
    public AsignacionMision(Ninja ninja, Mision mision) {
        this.ninja = ninja;
        this.mision = mision;
        this.fechaAsignacion = LocalDate.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Ninja getNinja() { return ninja; }
    public void setNinja(Ninja ninja) { this.ninja = ninja; }
    public Mision getMision() { return mision; }
    public void setMision(Mision mision) { this.mision = mision; }
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { 
        this.completada = completada; 
        if (completada) {
            this.fechaCompletacion = LocalDate.now();
        }
    }
    public LocalDate getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
    public LocalDate getFechaCompletacion() { return fechaCompletacion; }
    public void setFechaCompletacion(LocalDate fechaCompletacion) { this.fechaCompletacion = fechaCompletacion; }
}