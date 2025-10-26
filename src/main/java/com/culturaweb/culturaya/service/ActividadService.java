package com.culturaweb.culturaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Actividad;
import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.model.enums.Categoria;
import com.culturaweb.culturaya.model.enums.Rol;
import com.culturaweb.culturaya.repository.ActividadRepository;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private MovimientoService movimientoService;

    public List<Actividad> ListarActividades(){
        return actividadRepository.findAll();
    }

    public Actividad guardar(Actividad actividad){
        boolean esNuevo = actividad.getId() == null;

        if (esNuevo) {
            movimientoService.registrar("Actividad", "Creado", "Se creó la actividad: " + actividad.getTitulo());
        } else {
            movimientoService.registrar("Actividad", "Actualizado", "Se actualizó la actividad: " + actividad.getTitulo());
        }

        return actividadRepository.save(actividad);
    }

    public Actividad obtenerPorId(Long id){
        Optional<Actividad> actividadOpt = actividadRepository.findById(id);
        return actividadOpt.orElse(null);
    }

    public void eliminarActividad(Long id){
        Actividad actividad = actividadRepository.findById(id).orElse(null);
        if (actividad != null) {
            actividadRepository.delete(actividad);
            movimientoService.registrar("Actividad", "Eliminado", "Se eliminó la actividad: " + actividad.getTitulo());
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

    public long contarActividades(){
        return actividadRepository.count();
    }

    public List<Actividad> listarPorUsuario(Long usuarioId) {
        return actividadRepository.findByUsuarioId(usuarioId);
    }

    public List<Actividad> listarActividades(Usuario usuario) {
        if (usuario.getRol() == Rol.ADMIN) {
            return actividadRepository.findAll();
        } else {
            return actividadRepository.findByUsuarioId(usuario.getId());
        }
    }

    public Page<Actividad> listarEventosPaginable(Pageable pageable) {
        return actividadRepository.findByCategoria(Categoria.EVENTOS, pageable);
    }

    public Page<Actividad> listarConciertosPaginable(Pageable pageable) {
        return actividadRepository.findByCategoria(Categoria.CONCIERTOS, pageable);
    }

    public Page<Actividad> listarTeatroPaginable(Pageable pageable) {
        return actividadRepository.findByCategoria(Categoria.TEATRO, pageable);
    }

    public Page<Actividad> listarFeriasPaginable(Pageable pageable) {
        return actividadRepository.findByCategoria(Categoria.FERIAS, pageable);
    }

}
