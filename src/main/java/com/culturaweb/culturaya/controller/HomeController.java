package com.culturaweb.culturaya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.culturaweb.culturaya.model.enums.Categoria;
import com.culturaweb.culturaya.service.ActividadService;
import com.culturaweb.culturaya.service.NoticiaService;
import com.culturaweb.culturaya.service.ServicioService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ServicioService servicioService;
    private final NoticiaService noticiaService;
    private final ActividadService actividadService;


    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("noticias", noticiaService.listarNoticias());
        model.addAttribute("eventos", actividadService.listarPorCategoria(Categoria.EVENTOS));
        model.addAttribute("servicios", servicioService.listarServicios());
        
        return "index";
    }
    
    @GetMapping("/actividades")
    public String actividades(Model model) {
        model.addAttribute("conciertos", actividadService.listarPorCategoria(Categoria.CONCIERTOS));
        model.addAttribute("teatros", actividadService.listarPorCategoria(Categoria.TEATRO));
        model.addAttribute("ferias", actividadService.listarPorCategoria(Categoria.FERIAS));

        return "publico/Agenda";

    }
    
}
