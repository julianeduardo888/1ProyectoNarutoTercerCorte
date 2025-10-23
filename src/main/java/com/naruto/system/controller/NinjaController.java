package com.naruto.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naruto.system.domain.Ninja;
import com.naruto.system.service.NinjaService;

@RestController
@RequestMapping("/api/ninjas") // Todos los endpoints aquí empiezan con /api/ninjas
public class NinjaController {

    @Autowired
    private NinjaService ninjaService;

    // POST /api/ninjas
    @PostMapping
    public Ninja registrarNinja(@RequestBody NinjaService.NinjaDTO dto) {
        return ninjaService.registrarNinja(dto);
    }

    // GET /api/ninjas
    @GetMapping
    public List<Ninja> listarNinjas() {
        return ninjaService.listarNinjas();
    }

    // GET /api/ninjas/1
    @GetMapping("/{id}")
    public Ninja consultarNinja(@PathVariable Long id) {
        return ninjaService.consultarNinja(id);
    }
}