package com.culturaweb.culturaya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

@GetMapping({"","/dashboard"})
    public String dashboard(Model model) {
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
