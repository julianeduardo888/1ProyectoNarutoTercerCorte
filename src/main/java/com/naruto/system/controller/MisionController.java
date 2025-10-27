package com.naruto.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naruto.system.domain.AsignacionMision;
import com.naruto.system.domain.Mision;
import com.naruto.system.dto.MisionConNinjasDTO;
import com.naruto.system.service.MisionService;

@RestController
@RequestMapping("/api/misiones")
public class MisionController {

    @Autowired
    private MisionService misionService;

    // POST /api/misiones (para crear una plantilla de misión)
    @PostMapping
    public Mision registrarMision(@RequestBody MisionService.MisionDTO dto) {
        return misionService.registrarMision(dto);
    }

    // GET /api/misiones
    @GetMapping
    public List<MisionConNinjasDTO> listarMisiones() {
        return misionService.listarMisionesConNinjas();
    }

    // POST /api/misiones/asignar (Asignación simple, ya no se usa en el frontend)
    @PostMapping("/asignar")
    public AsignacionMision asignarMision(@RequestBody MisionService.AsignacionDTO dto) {
        return misionService.asignarMision(dto);
    }
    
    // NUEVO ENDPOINT PARA ASIGNAR EQUIPO
    @PostMapping("/asignar-equipo")
    public List<AsignacionMision> asignarEquipo(@RequestBody MisionService.AsignacionEquipoDTO dto) {
        return misionService.asignarEquipo(dto);
    }

    // PUT /api/misiones/completar/5 (donde 5 es el ID de la ASIGNACION)
    @PutMapping("/completar/{asignacionId}")
    public AsignacionMision completarMision(@PathVariable Long asignacionId) {
        return misionService.completarMision(asignacionId);
    }
}