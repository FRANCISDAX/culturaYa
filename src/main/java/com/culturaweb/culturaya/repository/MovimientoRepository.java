package com.culturaweb.culturaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.culturaweb.culturaya.model.entity.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long>{

    List<Movimiento> findTop5ByOrderByFechaDesc();
    Movimiento findFirstByOrderByFechaAsc();
    
}
