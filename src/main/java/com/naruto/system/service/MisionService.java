package com.naruto.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naruto.system.domain.AsignacionMision;
import com.naruto.system.domain.Mision;
import com.naruto.system.domain.Ninja;
import com.naruto.system.domain.Rango;
import com.naruto.system.domain.RangoMision; // Importante
import com.naruto.system.dto.MisionConNinjasDTO; // Importante
import com.naruto.system.dto.NinjaResumenDTO;
import com.naruto.system.repository.AsignacionMisionRepository;
import com.naruto.system.repository.MisionRepository;
import com.naruto.system.repository.NinjaRepository;

@Service
public class MisionService {

    @Autowired
    private MisionRepository misionRepository;
    @Autowired
    private NinjaRepository ninjaRepository;
    @Autowired
    private AsignacionMisionRepository asignacionRepository;

    // --- Gestión de Misiones ---

    public List<MisionConNinjasDTO> listarMisionesConNinjas() {
    // 1. Obtenemos todos los ninjas y misiones de la BD
    List<Mision> misiones = misionRepository.findAll();
    List<Ninja> ninjas = ninjaRepository.findAll();

    // 2. Creamos la lista de DTOs de resultado
    List<MisionConNinjasDTO> resultado = new ArrayList<>();

    // 3. Por cada misión...
    for (Mision mision : misiones) {
        // Convertimos la Mision a su DTO
        MisionConNinjasDTO misionDTO = new MisionConNinjasDTO(mision);

        // 4. Filtramos la lista de TODOS los ninjas para encontrar los elegibles
        List<NinjaResumenDTO> ninjasElegibles = ninjas.stream()
            .filter(ninja -> Rango.cumple(ninja.getRango(), mision.getRangoRequerido()))
            .map(ninja -> new NinjaResumenDTO(ninja)) // Convertimos el Ninja a NinjaResumenDTO
            .collect(Collectors.toList());

        // 5. Añadimos la lista de elegibles al DTO de la misión
        misionDTO.setNinjasElegibles(ninjasElegibles);

        // 6. Añadimos el DTO de la misión a la lista final
        resultado.add(misionDTO);
    }

    return resultado;
}

    public static record MisionDTO(String titulo, String descripcion, RangoMision rango, double recompensa, Rango rangoRequerido) {}

    public Mision registrarMision(MisionDTO dto) {
        Mision mision = new Mision(dto.titulo(), dto.descripcion(), dto.rango(), dto.recompensa(), dto.rangoRequerido());
        return misionRepository.save(mision);
    }

    // --- Asignación de Misiones ---

    public static record AsignacionDTO(Long ninjaId, Long misionId) {}

    public AsignacionMision asignarMision(AsignacionDTO dto) {
        Ninja ninja = ninjaRepository.findById(dto.ninjaId())
            .orElseThrow(() -> new RuntimeException("Ninja no encontrado"));
        Mision mision = misionRepository.findById(dto.misionId())
            .orElseThrow(() -> new RuntimeException("Misión no encontrada"));

        // Requisito: Validar que un ninja tenga el rango suficiente [cite: 150]
        if (!Rango.cumple(ninja.getRango(), mision.getRangoRequerido())) {
            throw new RuntimeException("El ninja " + ninja.getNombre() + " (Rango " + ninja.getRango() + 
                                     ") no cumple el rango requerido: " + mision.getRangoRequerido());
        }

        AsignacionMision asignacion = new AsignacionMision(ninja, mision);
        return asignacionRepository.save(asignacion);
    }

    // Requisito: Registrar que el ninja completó la misión [cite: 151]
    public AsignacionMision completarMision(Long asignacionId) {
        AsignacionMision asignacion = asignacionRepository.findById(asignacionId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        asignacion.setCompletada(true);
        return asignacionRepository.save(asignacion);
    }
}