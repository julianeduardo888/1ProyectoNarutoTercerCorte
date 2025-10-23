package com.naruto.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naruto.system.service.ReporteService;

@RestController
@RequestMapping("/api/reporte")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // GET /api/reporte/txt
    @GetMapping(value = "/txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getReporteTxt() {
        String reporte = reporteService.generarReporteTxt();
        return ResponseEntity.ok(reporte);
    }
}