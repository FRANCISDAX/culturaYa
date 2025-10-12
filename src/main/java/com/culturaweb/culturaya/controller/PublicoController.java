package com.culturaweb.culturaya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicoController {

    @GetMapping("/publico/Nosotros")
    public String nosotros() {
        return "publico/Nosotros";
    }

    @GetMapping("/publico/Agenda")
    public String agenda() {
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
