package com.culturaweb.culturaya.controller;
import com.culturaweb.culturaya.model.entity.Actividad;
import com.culturaweb.culturaya.model.entity.Noticia;
import com.culturaweb.culturaya.model.entity.Servicio;
import com.culturaweb.culturaya.service.ActividadService;
import com.culturaweb.culturaya.service.NoticiaService;
import com.culturaweb.culturaya.service.ServicioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;

@Controller
public class PublicoController {

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private ServicioService servicioService;

    // Página de inicio.
    @GetMapping("/")
    public String inicio(Model model,
        @RequestParam(defaultValue = "0") int pageEventos,
        @RequestParam(defaultValue = "0") int pageNoticias,
        @RequestParam(defaultValue = "0") int pageServicios) {

        Page<Noticia> noticias = noticiaService.listarNoticiasPaginable(PageRequest.of(pageNoticias, 3));
        model.addAttribute("noticias", noticias.getContent());
        model.addAttribute("paginaActualNoticias", noticias.getNumber());
        model.addAttribute("totalPaginasNoticias", noticias.getTotalPages());

        Page<Actividad> eventos = actividadService.listarEventosPaginable(PageRequest.of(pageEventos, 3));
        model.addAttribute("eventos", eventos.getContent());
        model.addAttribute("paginaActualEventos", eventos.getNumber());
        model.addAttribute("totalPaginasEventos", eventos.getTotalPages());

        Page<Servicio> servicios = servicioService.listarServiciosPaginable(PageRequest.of(pageServicios, 3));
        model.addAttribute("servicios", servicios.getContent());
        model.addAttribute("paginaActualServicios", servicios.getNumber());
        model.addAttribute("totalPaginasServicios", servicios.getTotalPages());

        return "index";
    }

    @GetMapping("/publico/Nosotros")
    public String nosotros() {
        return "publico/Nosotros";
    }

    @GetMapping("/publico/Agenda")
    public String agenda(Model model,
        @RequestParam(defaultValue = "0") int pageTeatro,
        @RequestParam(defaultValue = "0") int pageConciertos,
        @RequestParam(defaultValue = "0") int pageFerias) {

        Page<Actividad> teatro = actividadService.listarTeatroPaginable(PageRequest.of(pageTeatro, 3));
        model.addAttribute("teatros", teatro.getContent());
        model.addAttribute("paginaActualTeatro", teatro.getNumber());
        model.addAttribute("totalPaginasTeatro", teatro.getTotalPages());

        Page<Actividad> conciertos = actividadService.listarConciertosPaginable(PageRequest.of(pageConciertos, 3));
        model.addAttribute("conciertos", conciertos.getContent());
        model.addAttribute("paginaActualConciertos", conciertos.getNumber());
        model.addAttribute("totalPaginasConciertos", conciertos.getTotalPages());

        Page<Actividad> ferias = actividadService.listarFeriasPaginable(PageRequest.of(pageFerias, 3));
        model.addAttribute("ferias", ferias.getContent());
        model.addAttribute("paginaActualFerias", ferias.getNumber());
        model.addAttribute("totalPaginasFerias", ferias.getTotalPages());

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
