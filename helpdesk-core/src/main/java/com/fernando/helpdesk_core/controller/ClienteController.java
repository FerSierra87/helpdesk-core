package com.fernando.helpdesk_core.controller;


import com.fernando.helpdesk_core.entity.Cliente;
import com.fernando.helpdesk_core.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // GET /clientes -> lista todos los clientes
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // GET /clientes/5 -> busca un cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /clientes -> crea un cliente nuevo
    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // PUT /clientes/5 -> actualiza un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente datosNuevos) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    clienteExistente.setNombre(datosNuevos.getNombre());
                    clienteExistente.setEmail(datosNuevos.getEmail());
                    clienteExistente.setTelefono(datosNuevos.getTelefono());
                    return ResponseEntity.ok(clienteRepository.save(clienteExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /clientes/5 -> elimina un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
