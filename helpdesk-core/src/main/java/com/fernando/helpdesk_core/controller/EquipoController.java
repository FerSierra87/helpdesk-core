package com.fernando.helpdesk_core.controller;

import com.fernando.helpdesk_core.entity.Equipo;
import com.fernando.helpdesk_core.repository.ClienteRepository;
import com.fernando.helpdesk_core.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Equipo> listarTodos() {
        return equipoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> buscarPorId(@PathVariable Long id) {
        return equipoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /equipos/cliente/3 -> todos los equipos del cliente con id 3
    @GetMapping("/cliente/{clienteId}")
    public List<Equipo> listarPorCliente(@PathVariable Long clienteId) {
        return equipoRepository.findByClienteId(clienteId);
    }

    @PostMapping
    public ResponseEntity<Equipo> crear(@RequestBody Equipo equipo) {
        if (equipo.getCliente() == null || equipo.getCliente().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return clienteRepository.findById(equipo.getCliente().getId())
                .map(cliente -> {
                    equipo.setCliente(cliente);
                    return ResponseEntity.ok(equipoRepository.save(equipo));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipo> actualizar(@PathVariable Long id, @RequestBody Equipo datosNuevos) {
        return equipoRepository.findById(id)
                .map(equipoExistente -> {
                    equipoExistente.setTipo(datosNuevos.getTipo());
                    equipoExistente.setMarca(datosNuevos.getMarca());
                    equipoExistente.setModelo(datosNuevos.getModelo());
                    equipoExistente.setNumeroSerie(datosNuevos.getNumeroSerie());
                    return ResponseEntity.ok(equipoRepository.save(equipoExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!equipoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}