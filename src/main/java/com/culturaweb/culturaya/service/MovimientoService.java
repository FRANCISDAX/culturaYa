package com.culturaweb.culturaya.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Movimiento;
import com.culturaweb.culturaya.repository.MovimientoRepository;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    public void registrar(String entidad, String accion, String descripcion) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuario = (auth != null) ? auth.getName() : "SISTEMA";
        Movimiento m = new Movimiento(entidad, accion, descripcion, usuario);
        movimientoRepository.save(m);

        long total = movimientoRepository.count();
        if (total > 20) {
            Movimiento masAntiguo = movimientoRepository.findFirstByOrderByFechaAsc();
            if (masAntiguo != null) {
                movimientoRepository.delete(masAntiguo);
            }
        }
        
    }


    public List<Movimiento> ultimosCinco() {
        return movimientoRepository.findTop5ByOrderByFechaDesc();
    }

}
