package com.culturaweb.culturaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.culturaweb.culturaya.model.entity.Noticia;
import com.culturaweb.culturaya.repository.NoticiaRepository;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private MovimientoService movimientoService;

    public Noticia guardar(Noticia noticia){
        boolean esNuevo = noticia.getId() == null;

        if (esNuevo) {
            movimientoService.registrar("Noticia", "Creado", "Se creó la noticia: " + noticia.getTitulo());
        } else {
            movimientoService.registrar("Noticia", "Actualizado", "Se actualizó la noticia: " + noticia.getTitulo());
        }

        return noticiaRepository.save(noticia);
    }

    public List<Noticia> listarNoticias(){
        return noticiaRepository.findAll();
    }

    public Noticia obtenerPorId(Long id) {
        Optional<Noticia> noticiaOpt = noticiaRepository.findById(id);
        return noticiaOpt.orElse(null);
    }
    
    public void eliminarNoticia(Long id){
        Noticia noticia = noticiaRepository.findById(id).orElse(null);
        if (noticia != null) {
            noticiaRepository.delete(noticia);
            movimientoService.registrar("Noticia", "Eliminado", "Se eliminó la noticia: " + noticia.getTitulo());
        }
    }

    public long contarNoticias(){
        return noticiaRepository.count();
    }
    
}
