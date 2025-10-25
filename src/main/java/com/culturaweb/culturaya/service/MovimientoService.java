package com.culturaweb.culturaya.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Movimiento;
import com.culturaweb.culturaya.repository.MovimientoRepository;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    public void registrar(String entidad, String accion, String descripcion) {
        Movimiento m = new Movimiento(entidad, accion, descripcion);
        movimientoRepository.save(m);
    }

    public List<Movimiento> ultimosCinco() {
        return movimientoRepository.findTop5ByOrderByFechaDesc();
    }

}
