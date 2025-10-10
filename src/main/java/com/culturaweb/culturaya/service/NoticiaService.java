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

    public Noticia guardar(Noticia noticia){
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
        if (noticiaRepository.existsById(id)){
            noticiaRepository.deleteById(id);
        }
    }
    
}
