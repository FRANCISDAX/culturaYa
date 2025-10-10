package com.culturaweb.culturaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Actividad;
import com.culturaweb.culturaya.model.enums.Categoria;
import com.culturaweb.culturaya.repository.ActividadRepository;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    public List<Actividad> ListarActividades(){
        return actividadRepository.findAll();
    }

    public Actividad guardar(Actividad actividad){
        return actividadRepository.save(actividad);
    }

    public Actividad obtenerPorId(Long id){
        Optional<Actividad> actividadOpt = actividadRepository.findById(id);
        return actividadOpt.orElse(null);
    }

    public void eliminarActividad(Long id){
        if( actividadRepository.existsById(id)){
            actividadRepository.deleteById(id);
        }
    }

    public List<Actividad> listarPorCategoria(Categoria categoria){
        return actividadRepository.findByCategoria(categoria);
    }

    public List<Actividad> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) {
            return actividadRepository.findAll();
        }
        Categoria catEnum = Categoria.valueOf(categoria.toUpperCase());
        return actividadRepository.findByCategoria(catEnum);
    }
}
