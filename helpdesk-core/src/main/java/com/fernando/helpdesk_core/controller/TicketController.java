package com.fernando.helpdesk_core.controller;

import com.fernando.helpdesk_core.entity.Ticket;
import com.fernando.helpdesk_core.repository.EquipoRepository;
import com.fernando.helpdesk_core.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @GetMapping
    public List<Ticket> listarTodos() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> buscarPorId(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/equipo/{equipoId}")
    public List<Ticket> listarPorEquipo(@PathVariable Long equipoId) {
        return ticketRepository.findByEquipoId(equipoId);
    }

    @GetMapping("/estado/{estado}")
    public List<Ticket> listarPorEstado(@PathVariable String estado) {
        return ticketRepository.findByEstado(estado);
    }

    @PostMapping
    public ResponseEntity<Ticket> crear(@RequestBody Ticket ticket) {
        if (ticket.getEquipo() == null || ticket.getEquipo().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return equipoRepository.findById(ticket.getEquipo().getId())
                .map(equipo -> {
                    ticket.setEquipo(equipo);
                    return ResponseEntity.ok(ticketRepository.save(ticket));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<Ticket> resolver(@PathVariable Long id) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setEstado("resuelto");
                    return ResponseEntity.ok(ticketRepository.save(ticket));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ticketRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}