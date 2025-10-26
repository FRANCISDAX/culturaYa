package com.culturaweb.culturaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.service.ActividadService;
import com.culturaweb.culturaya.service.MovimientoService;
import com.culturaweb.culturaya.service.NoticiaService;
import com.culturaweb.culturaya.service.ServicioService;
import com.culturaweb.culturaya.service.UsuarioService;

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

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"","/dashboard"})
    public String dashboard(Model model) {
        long totalUsuarios = usuarioService.contarUsuarios();
        long totalUActivos = usuarioService.contarUsuariosActivos();
        long totalUInactivos = usuarioService.contarUsuariosInactivos();
        long totalNoticias = noticiaService.contarNoticias();
        long totalActividades = actividadService.contarActividades();
        long totalServicios = servicioService.contarServicios();

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosActivos", totalUActivos);
        model.addAttribute("usuariosInactivos", totalUInactivos);
        model.addAttribute("totalNoticias", totalNoticias);
        model.addAttribute("totalActividades", totalActividades);
        model.addAttribute("totalServicios", totalServicios);
        model.addAttribute("movimientos", movimientoService.ultimosCinco());
        model.addAttribute("vista", "adminDashboard");
        model.addAttribute("titulo", "Dashboard - Panel de Administración");
        return "privado/layout_admin";
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        var usuario = usuarioService.buscarPorEmail(email);

        model.addAttribute("usuario", usuario);
        model.addAttribute("vista", "adminPerfil");
        return "privado/layout_admin";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute("usuario") Usuario usuarioForm,
                                RedirectAttributes redirectAttrs) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Usuario usuarioActual = usuarioService.buscarPorEmail(email);

            usuarioActual.setNombre(usuarioForm.getNombre());
            usuarioActual.setTelefono(usuarioForm.getTelefono());
            usuarioActual.setInstitucion(usuarioForm.getInstitucion());

            usuarioService.guardarPerfil(usuarioActual);

            redirectAttrs.addFlashAttribute("exito", "Perfil actualizado correctamente.");

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Ocurrió un error al actualizar el perfil.");
        }

        return "redirect:/admin/perfil";
    }

}
