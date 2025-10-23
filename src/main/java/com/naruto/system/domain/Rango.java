package com.naruto.system.domain;

public enum Rango {
    GENIN, 
    CHUNIN, 
    JONIN;

    /**
     * Obtiene el nivel numérico del rango para comparaciones.
     */
    public static int nivel(Rango r) {
    if (r == null) return 1;
    // Esta es la "expresión switch" moderna que el IDE sugiere
    return switch (r) {
        case GENIN -> 1;
        case CHUNIN -> 2;
        case JONIN -> 3;
    };
}
    /**
     * Verifica si un ninja (rango 'real') cumple con un 'rangoRequerido'.
     * Esto será útil para la asignación de misiones[cite: 225].
     */
    public static boolean cumple(Rango real, Rango requerido) {
        if (real == null || requerido == null) return false;
        return nivel(real) >= nivel(requerido);
    }
}