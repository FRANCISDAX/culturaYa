package com.culturaweb.culturaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/usuarios")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ------------------------------------------------------------
    // Mostrar lista de usuarios.
    // ------------------------------------------------------------
    @GetMapping
    public String listar(Model model,
                         @RequestParam(value = "exito", required = false) String exito,
                         @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("vista", "adminUsuarios");
        if (exito != null) model.addAttribute("exito", exito);
        if (error != null) model.addAttribute("error", error);
        return "privado/layout_admin";
    }

    // ------------------------------------------------------------
    // Guardar nuevo usuario.
    // ------------------------------------------------------------
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            model.addAttribute("vista", "adminUsuarios");
            model.addAttribute("error", "Por favor, corrige los errores del formulario.");
            return "privado/layout_admin";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            return "redirect:/admin/usuarios?exito=Usuario registrado correctamente";
        } catch (Exception e) {
            return "redirect:/admin/usuarios?error=No se pudo registrar el usuario";
        }
    }

    // ------------------------------------------------------------
    // Editar usuario.
    // ------------------------------------------------------------
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario == null) {
            return "redirect:/admin/usuarios?error=Usuario no encontrado";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("vista", "adminUsuarios");
        model.addAttribute("modoEditar", true);
        return "privado/layout_admin";
    }

    // ------------------------------------------------------------
    // Eliminar usuario.
    // ------------------------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return "redirect:/admin/usuarios?exito=Usuario eliminado correctamente";
        } catch (Exception e) {
            return "redirect:/admin/usuarios?error=No se pudo eliminar el usuario";
        }
    }

    // ------------------------------------------------------------
    // Buscar usuario por ID
    // ------------------------------------------------------------
    @GetMapping("/buscar/{id}")
    @ResponseBody
    public Usuario buscarUsuario(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @GetMapping("/existeEmail")
    @ResponseBody
    public boolean existeEmail(@RequestParam String email, @RequestParam(required = false) Long id) {
        Usuario existente = usuarioService.buscarPorEmail(email);

        if (existente != null && (id == null || !existente.getId().equals(id))) {
            return true;
        }

        return false;
        
    }

}
