package com.fernando.helpdesk_core.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // ej: "Notebook", "Monitor", "Impresora"

    @Column(nullable = false)
    private String marca;

    private String modelo;

    @Column(unique = true)
    private String numeroSerie;

    // ------ RELACIÓN CON CLIENTE ------
    // Muchos equipos pueden pertenecer a un mismo cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}