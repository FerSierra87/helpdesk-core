package com.fernando.helpdesk_core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String categoria; // "Hardware", "Redes", "Accesos", "Sistemas"

    @Column(nullable = false)
    private String prioridad; // "baja", "media", "alta"

    @Column(nullable = false)
    private String estado = "abierto"; // "abierto" o "resuelto"

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // ------ RELACIÓN CON EQUIPO ------
    // Muchos tickets pueden pertenecer a un mismo equipo
    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;
}