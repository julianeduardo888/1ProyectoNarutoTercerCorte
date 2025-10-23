package com.naruto.system.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naruto.system.domain.Aldea;
import com.naruto.system.domain.ChakraProfile;
import com.naruto.system.domain.Jutsu; // Importante para inicializar datos
import com.naruto.system.domain.JutsuTipo;
import com.naruto.system.domain.Ninja;
import com.naruto.system.domain.Rango;
import com.naruto.system.domain.Stats;
import com.naruto.system.repository.AldeaRepository;
import com.naruto.system.repository.JutsuRepository;
import com.naruto.system.repository.NinjaRepository;

import jakarta.annotation.PostConstruct;

@Service
public class NinjaService {

    @Autowired
    private NinjaRepository ninjaRepository;
    @Autowired
    private AldeaRepository aldeaRepository;
    @Autowired
    private JutsuRepository jutsuRepository;

    /**
     * Este método se ejecuta cuando la aplicación arranca.
     * Lo usamos para precargar las Aldeas y Jutsus en la BD.
     */
    @PostConstruct
    public void initDb() {
        // Guardar Aldeas (YA NO guardamos en variables 'konoha' y 'suna')
        aldeaRepository.findByNombre("Konoha").orElseGet(() -> aldeaRepository.save(new Aldea("Konoha")));
        aldeaRepository.findByNombre("Suna").orElseGet(() -> aldeaRepository.save(new Aldea("Suna")));
        // ... puedes añadir más aldeas si quieres

        // Guardar Jutsus (Estos ya estaban bien)
        jutsuRepository.findByNombre("Rasengan").orElseGet(() -> jutsuRepository.save(new Jutsu("Rasengan", JutsuTipo.NINJUTSU)));
        jutsuRepository.findByNombre("Chidori").orElseGet(() -> jutsuRepository.save(new Jutsu("Chidori", JutsuTipo.NINJUTSU)));
        jutsuRepository.findByNombre("Kage Bunshin no Jutsu").orElseGet(() -> jutsuRepository.save(new Jutsu("Kage Bunshin no Jutsu", JutsuTipo.CLONACION)));
        jutsuRepository.findByNombre("Control de Arena").orElseGet(() -> jutsuRepository.save(new Jutsu("Control de Arena", JutsuTipo.NINJUTSU)));
        jutsuRepository.findByNombre("Sharingan").orElseGet(() -> jutsuRepository.save(new Jutsu("Sharingan", JutsuTipo.DOJUTSU)));
        jutsuRepository.findByNombre("Rasenshuriken").orElseGet(() -> jutsuRepository.save(new Jutsu("Rasenshuriken", JutsuTipo.NINJUTSU)));
}

    // Requisito: Listar ninjas
    public List<Ninja> listarNinjas() {
        return ninjaRepository.findAll();
    }
    
    // Requisito: Consultar ninja
    public Ninja consultarNinja(Long id) {
        return ninjaRepository.findById(id).orElseThrow(() -> new RuntimeException("Ninja no encontrado"));
    }

    // Requisito: Registrar ninja
    // Usaremos DTOs (Data Transfer Objects) para recibir los datos del frontend
    public static record NinjaDTO(String nombre, Rango rango, String nombreAldea, int ataque, int defensa, Set<String> nombresJutsu) {}
    
    public Ninja registrarNinja(NinjaDTO dto) {
        // 1. Buscar la Aldea en la BD
        Aldea aldea = aldeaRepository.findByNombre(dto.nombreAldea())
            .orElseThrow(() -> new RuntimeException("Aldea no encontrada: " + dto.nombreAldea()));

        // 2. Crear Stats y ChakraProfile
        ChakraProfile chakra = new ChakraProfile(); // Simple por ahora
        Stats stats = new Stats(dto.ataque(), dto.defensa(), chakra);

        // 3. Crear el Ninja
        Ninja ninja = new Ninja(dto.nombre(), dto.rango(), aldea, stats);

        // 4. Buscar y añadir Jutsus
        for (String nombreJutsu : dto.nombresJutsu()) {
            Jutsu jutsu = jutsuRepository.findByNombre(nombreJutsu)
                .orElseThrow(() -> new RuntimeException("Jutsu no encontrado: " + nombreJutsu));
            ninja.aprenderJutsu(jutsu);
        }

        // 5. Guardar en la BD
        return ninjaRepository.save(ninja);
    }
}