package com.fernando.helpdesk_core.repository;

import com.fernando.helpdesk_core.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    List<Equipo> findByClienteId(Long clienteId);
}