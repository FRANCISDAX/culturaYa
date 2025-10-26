package com.culturaweb.culturaya.controller;
import com.culturaweb.culturaya.service.ActividadService;
import com.culturaweb.culturaya.service.NoticiaService;
import com.culturaweb.culturaya.service.ServicioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicoController {

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private ServicioService servicioService;

    // Página de inicio (todos los datos visibles)
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("noticias", noticiaService.listarNoticias());
        model.addAttribute("eventos", actividadService.buscarPorCategoria("EVENTOS"));
        model.addAttribute("servicios", servicioService.listarServicios());
        return "index";
    }

    @GetMapping("/publico/Nosotros")
    public String nosotros() {
        return "publico/Nosotros";
    }

    @GetMapping("/publico/Agenda")
    public String agenda(Model model) {
        model.addAttribute("teatros", actividadService.buscarPorCategoria("TEATRO"));
        model.addAttribute("conciertos", actividadService.buscarPorCategoria("CONCIERTOS"));
        model.addAttribute("ferias", actividadService.buscarPorCategoria("FERIAS"));
        return "publico/Agenda";
    }

    @GetMapping("/publico/Aliados")
    public String aliados() {
        return "publico/Aliados";
    }

    @GetMapping("/publico/Contacto")
    public String contacto() {
        return "publico/Contacto";
    }

}
