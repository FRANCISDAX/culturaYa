package com.culturaweb.culturaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Servicio;

@Service
public interface ServicioRepository extends JpaRepository<Servicio, Long>{
}
