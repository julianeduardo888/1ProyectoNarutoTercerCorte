package com.naruto.system.service;

import com.naruto.system.domain.Mision;
import com.naruto.system.domain.Ninja;
import com.naruto.system.repository.MisionRepository;
import com.naruto.system.repository.NinjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private NinjaRepository ninjaRepository;
    @Autowired
    private MisionRepository misionRepository;

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
              //.append(", Chakra=").append(n.getStats().getChakra().resumen()).append("\n") // Podrías añadir el resumen si lo implementaste
              .append("  Jutsus: ").append(n.getJutsus()).append("\n\n");
        }

        sb.append("=== REPORTE DE MISIONES ===\n\n");
        List<Mision> misiones = misionRepository.findAll();
        for (Mision m : misiones) {
            sb.append("[MISION] ").append(m.getTitulo()).append(" | Rango ").append(m.getRango())
              .append(" | Recompensa ").append(m.getRecompensa())
              .append(" | Requiere: ").append(m.getRangoRequerido()).append("\n")
              .append("  ").append(m.getDescripcion()).append("\n\n");
        }

        return sb.toString();
    }
}