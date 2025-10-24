package com.culturaweb.culturaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Servicio;
import com.culturaweb.culturaya.repository.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public Servicio guardar(Servicio servicio){
        return servicioRepository.save(servicio);
    }

    public List<Servicio> listarServicios(){
        return servicioRepository.findAll();
    }

    public Servicio obtenerPorId(Long id) {
        Optional<Servicio> servicioOpt = servicioRepository.findById(id);
        return servicioOpt.orElse(null);
    }
    
    public void eliminarServicio(Long id){
        if (servicioRepository.existsById(id)){
            servicioRepository.deleteById(id);
        }
    }

    public long contarServicios(){
        return servicioRepository.count();
    }
    
}
