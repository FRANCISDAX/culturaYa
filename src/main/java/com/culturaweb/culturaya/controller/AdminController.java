package com.culturaweb.culturaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.culturaweb.culturaya.service.ActividadService;
import com.culturaweb.culturaya.service.MovimientoService;
import com.culturaweb.culturaya.service.NoticiaService;
import com.culturaweb.culturaya.service.ServicioService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping({"","/dashboard"})
    public String dashboard(Model model) {
        long totalNoticias = noticiaService.contarNoticias();
        long totalActividades = actividadService.contarActividades();
        long totalServicios = servicioService.contarServicios();

        model.addAttribute("totalNoticias", totalNoticias);
        model.addAttribute("totalActividades", totalActividades);
        model.addAttribute("totalServicios", totalServicios);
         model.addAttribute("movimientos", movimientoService.ultimosCinco());
        model.addAttribute("vista", "adminDashboard");
        model.addAttribute("titulo", "Dashboard - Panel de Administración");
        return "privado/layout_admin";
    }
            
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("vista", "adminUsuarios");
        model.addAttribute("titulo", "Usuarios - Panel de Administración");
        return "privado/layout_admin";
    }
    
    @GetMapping("/perfil")
    public String perfil(Model model) {
        return "privado/layout_admin";
    }

}
