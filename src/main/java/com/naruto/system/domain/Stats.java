package com.naruto.system.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class Stats {

    private int ataque;
    private int defensa;

    @Embedded // Incrustamos el perfil de chakra aquí
    private ChakraProfile chakra;

    // Constructor vacío requerido por JPA
    public Stats() {
        this.chakra = new ChakraProfile();
    }

    public Stats(int ataque, int defensa, ChakraProfile chakra) {
        this.ataque = Math.max(0, ataque);
        this.defensa = Math.max(0, defensa);
        this.chakra = chakra != null ? chakra : new ChakraProfile();
    }

    // Getters, Setters y tus métodos 'incAtaque'
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }
    public ChakraProfile getChakra() { return chakra; }
    public void setChakra(ChakraProfile chakra) { this.chakra = chakra; }
    
    public void incAtaque(int v){ this.ataque = Math.max(0, this.ataque + v); }
    public void incDefensa(int v){ this.defensa = Math.max(0, this.defensa + v); }
}