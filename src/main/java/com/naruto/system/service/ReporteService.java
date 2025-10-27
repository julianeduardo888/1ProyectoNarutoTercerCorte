package com.naruto.system.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naruto.system.domain.AsignacionMision; // Importante
import com.naruto.system.domain.Mision;
import com.naruto.system.domain.Ninja;
import com.naruto.system.repository.AsignacionMisionRepository; // Importante
import com.naruto.system.repository.MisionRepository;
import com.naruto.system.repository.NinjaRepository;

@Service
public class ReporteService {

    @Autowired
    private NinjaRepository ninjaRepository;
    @Autowired
    private MisionRepository misionRepository;
    @Autowired
    private AsignacionMisionRepository asignacionRepository; // NUEVO

    // Reemplazo del Patrón Visitor
    public String generarReporteTxt() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE NINJAS ===\n\n");

        List<Ninja> ninjas = ninjaRepository.findAll();
        for (Ninja n : ninjas) {
            sb.append("[NINJA] ").append(n.getNombre()).append(" | ")
                    .append(n.getAldea().getNombre()).append(" | ").append(n.getRango()).append("\n")
                    .append("  Stats: ATQ=").append(n.getStats().getAtaque())
                    .append(", DEF=").append(n.getStats().getDefensa()).append("\n")
                    .append("  Jutsus: ").append(n.getJutsus()).append("\n\n");
        }

        sb.append("=== REPORTE DE MISIONES ===\n\n");
        List<Mision> misiones = misionRepository.findAll();
        for (Mision m : misiones) {
            sb.append("[MISION] ").append(m.getTitulo()).append(" | Rango ").append(m.getRango())
                    .append(" | Recompensa ").append(m.getRecompensa())
                    .append(" | Requiere: ").append(m.getRangoRequerido()).append("\n");

            // --- LÓGICA PARA MOSTRAR EQUIPO ASIGNADO ---
            List<AsignacionMision> asignaciones = asignacionRepository.findByMisionId(m.getId());
            if (!asignaciones.isEmpty()) {
                sb.append("  >> Equipo Asignado:\n");
                for (AsignacionMision asig : asignaciones) {
                    // Obtenemos el ninja de la asignación
                    String estado = asig.isCompletada() ? " (Completada)" : " (En Progreso)";
                    sb.append("     - ").append(asig.getNinja().getNombre()).append(estado).append("\n");
                }
            }
            // --- FIN LÓGICA DE EQUIPO ---

            sb.append("  ").append(m.getDescripcion()).append("\n\n");
        }

        // --- LÓGICA DE GUARDADO ---
        try {
            // "export" será una carpeta en la raíz del proyecto (dentro de /app en Docker)
            Path exportDir = Paths.get("export");
            if (!Files.exists(exportDir)) {
                Files.createDirectories(exportDir);
            }
            Path filePath = exportDir.resolve("reporte_ninjas_misiones.txt");

            // Escribe el contenido del StringBuilder al archivo
            Files.writeString(filePath, sb.toString(), StandardCharsets.UTF_8);

            // Imprime en la consola del SERVIDOR (la terminal de Docker)
            System.out.println("Reporte guardado exitosamente en: " + filePath.toAbsolutePath());

        } catch (IOException e) {
            // Si falla el guardado, solo imprime el error pero no detengas la app
            System.err.println("Error al guardar el reporte TXT: " + e.getMessage());
        }
        // --- FIN LÓGICA DE GUARDADO ---

        return sb.toString(); // Devuelve el texto al frontend como antes
    }
}