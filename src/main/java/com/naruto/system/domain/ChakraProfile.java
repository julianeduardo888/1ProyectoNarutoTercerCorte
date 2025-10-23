package com.naruto.system.domain;

import java.util.EnumSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

@Embeddable // Esta clase puede ser incrustada en otra entidad
public class ChakraProfile {

    // JPA guardará esta colección de Enums en una tabla separada
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ninja_afinidades", joinColumns = @JoinColumn(name = "ninja_id"))
    @Enumerated(EnumType.STRING)
    private Set<Elemento> afinidades = EnumSet.noneOf(Elemento.class);

    private boolean yin;
    private boolean yang;
    // ... (otros campos que tenías)

    // Getters, Setters y métodos (los tuyos de 'resumen', etc.)
    // ... (Tu código existente de getters/setters/addAfinidad va aquí)
    
    public Set<Elemento> getAfinidades() { return afinidades; }
    public void setAfinidades(Set<Elemento> afinidades) { this.afinidades = afinidades; }
    public boolean isYin() { return yin; }
    public void setYin(boolean yin) { this.yin = yin; }
    public boolean isYang() { return yang; }
    public void setYang(boolean yang) { this.yang = yang; }
    
    // ... (Asegúrate de tener un constructor vacío si añades uno con parámetros)
    public ChakraProfile() {}
    
    // ... (Tu método resumen() y otros)
    public String resumen() {
        // ... (Tu lógica de resumen)
        return afinidades.toString() + (yin ? " | Yin" : "") + (yang ? " | Yang" : "");
    }
}