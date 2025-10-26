package com.culturaweb.culturaya.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.model.enums.Rol;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(Rol rol);
    boolean existsByRol(Rol rol);
    long countByEstadoTrue();
    long countByEstadoFalse();

}
