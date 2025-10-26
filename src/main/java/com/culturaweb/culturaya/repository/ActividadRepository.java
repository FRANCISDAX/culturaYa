package com.culturaweb.culturaya.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.culturaweb.culturaya.model.entity.Actividad;
import com.culturaweb.culturaya.model.enums.Categoria;


@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long>{

    List<Actividad> findByUsuarioId(Long usuarioId);
    List<Actividad> findByCategoria(Categoria categoria);
    Page<Actividad> findByCategoria(Categoria categoria, Pageable pageable);

}
