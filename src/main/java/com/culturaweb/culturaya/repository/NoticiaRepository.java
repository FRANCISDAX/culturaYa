package com.culturaweb.culturaya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.culturaweb.culturaya.model.entity.Noticia;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Long>  {

    List<Noticia> findByUsuarioId(Long usuarioId);
    
}
