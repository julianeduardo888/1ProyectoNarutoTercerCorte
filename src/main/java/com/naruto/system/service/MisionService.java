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
import com.naruto.system.domain.RangoMision;
import com.naruto.system.dto.MisionConNinjasDTO;
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

    // DTO para la asignación simple (ya no se usa en el frontend, pero puede quedar)
    public static record AsignacionDTO(Long ninjaId, Long misionId) {}

    // DTO para la nueva asignación de equipo
    public static record AsignacionEquipoDTO(Long misionId, List<Long> ninjaIds) {}

    // Método para asignar un solo ninja (ya no se usa en el frontend)
    public AsignacionMision asignarMision(AsignacionDTO dto) {
        Ninja ninja = ninjaRepository.findById(dto.ninjaId())
                .orElseThrow(() -> new RuntimeException("Ninja no encontrado"));
        Mision mision = misionRepository.findById(dto.misionId())
                .orElseThrow(() -> new RuntimeException("Misión no encontrada"));

        // Requisito: Validar que un ninja tenga el rango suficiente
        if (!Rango.cumple(ninja.getRango(), mision.getRangoRequerido())) {
            throw new RuntimeException("El ninja " + ninja.getNombre() + " (Rango " + ninja.getRango() + 
                                     ") no cumple el rango requerido: " + mision.getRangoRequerido());
        }

        AsignacionMision asignacion = new AsignacionMision(ninja, mision);
        return asignacionRepository.save(asignacion);
    }
    
    // NUEVO MÉTODO PARA ASIGNAR UN EQUIPO
    public List<AsignacionMision> asignarEquipo(AsignacionEquipoDTO dto) {
        Mision mision = misionRepository.findById(dto.misionId())
            .orElseThrow(() -> new RuntimeException("Misión no encontrada"));

        List<Ninja> ninjas = ninjaRepository.findAllById(dto.ninjaIds());
        
        if (ninjas.size() != dto.ninjaIds().size()) {
            throw new RuntimeException("Uno o más ninjas no fueron encontrados.");
        }

        List<AsignacionMision> asignacionesGuardadas = new ArrayList<>();
        
        // Validamos a TODOS los ninjas primero
        for (Ninja ninja : ninjas) {
            if (!Rango.cumple(ninja.getRango(), mision.getRangoRequerido())) {
                throw new RuntimeException("El ninja " + ninja.getNombre() + 
                                         " no cumple el rango requerido para la misión.");
            }
        }

        // Si todos son válidos, los asignamos
        for (Ninja ninja : ninjas) {
            AsignacionMision nuevaAsignacion = new AsignacionMision(ninja, mision);
            asignacionesGuardadas.add(asignacionRepository.save(nuevaAsignacion));
        }

        return asignacionesGuardadas;
    }


    // Requisito: Registrar que el ninja completó la misión
    public AsignacionMision completarMision(Long asignacionId) {
        AsignacionMision asignacion = asignacionRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        asignacion.setCompletada(true);
        return asignacionRepository.save(asignacion);
    }
}