package com.culturaweb.culturaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Servicio;
import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.model.enums.Rol;
import com.culturaweb.culturaya.repository.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private MovimientoService movimientoService;

    public Servicio guardar(Servicio servicio){
        boolean esNuevo = servicio.getId() == null;

        if (esNuevo) {
            movimientoService.registrar("Servicio", "Creado", "Se creó el servicio: " + servicio.getTitulo());
        } else {
            movimientoService.registrar("Servicio", "Actualizado", "Se actualizó el servicio: " + servicio.getTitulo());
        }

        return servicioRepository.save(servicio);
    }

    public List<Servicio> listarServicios(){
        return servicioRepository.findAll();
    }

    public List<Servicio> listarPorUsuario(Long usuarioId) {
        return servicioRepository.findByUsuarioId(usuarioId);
    }

    public List<Servicio> listarServicios(Usuario usuario) {
        if (usuario.getRol() == Rol.ADMIN) {
            return servicioRepository.findAll();
        } else {
            return servicioRepository.findByUsuarioId(usuario.getId());
        }
    }

    public Servicio obtenerPorId(Long id) {
        Optional<Servicio> servicioOpt = servicioRepository.findById(id);
        return servicioOpt.orElse(null);
    }
    
    public void eliminarServicio(Long id){
        Servicio servicio = servicioRepository.findById(id).orElse(null);
        if (servicio != null) {
            servicioRepository.delete(servicio);
            movimientoService.registrar("Servicio", "Eliminado", "Se eliminó el servicio: " + servicio.getTitulo());
        }    }

    public long contarServicios(){
        return servicioRepository.count();
    }
    
    public Page<Servicio> listarServiciosPaginable(Pageable pageable){
        return servicioRepository.findAll(pageable);
    }

}
