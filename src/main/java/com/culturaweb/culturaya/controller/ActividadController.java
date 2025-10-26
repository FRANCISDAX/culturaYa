package com.culturaweb.culturaya.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.culturaweb.culturaya.configuration.CustomUserDetails;
import com.culturaweb.culturaya.model.entity.Actividad;
import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.service.ActividadService;
import com.culturaweb.culturaya.service.CloudinaryService;
import com.culturaweb.culturaya.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/actividades")
public class ActividadController {

    @Autowired
    ActividadService actividadService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        if (usuario.getRol().name().equals("ADMIN")) {
            model.addAttribute("actividades", actividadService.ListarActividades());
        } else {
            model.addAttribute("actividades", actividadService.listarPorUsuario(usuario.getId()));
        }

        model.addAttribute("actividad", new Actividad());
        model.addAttribute("vista", "adminActividades");
        return "privado/layout_admin";
    }

    @PostMapping("/guardar")
    public String guardar(
        @Valid Actividad actividad,
        BindingResult result,
        @RequestParam("imagenFile") MultipartFile imagenFile,
        RedirectAttributes attr) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (result.hasErrors()) {
            result.getAllErrors().forEach(err -> System.out.println("Error: " + err.getDefaultMessage()));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.actividad", result);
            attr.addFlashAttribute("actividad", actividad);
            return "redirect:/admin/actividades";
        }

        try {
            // Validar si hay imágen subida.
            if (actividad.getId() == null && (imagenFile == null || imagenFile.isEmpty())) {
                attr.addFlashAttribute("error", "Debe seleccionar una imagen.");
                return "redirect:/admin/actividades";
            }

            if (imagenFile != null && !imagenFile.isEmpty()) {
                // Subir imagen a Cloudinary.
                Map<String, Object> uploadResult = cloudinaryService.uploadFile(imagenFile);
                // Guardar la URL y el public_id.
                actividad.setImagen((String) uploadResult.get("secure_url"));
                actividad.setImagenPublicId((String) uploadResult.get("public_id"));
            }
            
            // Guardar en la Base de Datos.
            actividad.setUsuario(usuario);
            actividadService.guardar(actividad);
            attr.addFlashAttribute("exito", "Actividad registrado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al guardar la actividad: " + e.getMessage());
        }

        return "redirect:/admin/actividades";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Actividad actividad = actividadService.obtenerPorId(id);

        if (actividad == null) {
            attr.addFlashAttribute("error", "La actividad no existe o fue eliminada.");
            return "redirect:/admin/actividades";
        }

        model.addAttribute("actividades", actividadService.ListarActividades());
        model.addAttribute("actividadEditar", actividad);
        model.addAttribute("vista", "adminActividades");
        return "privado/layout_admin";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @Valid Actividad actividad,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("actividad", actividad);
            return "redirect:/admin/actividades";
        }

        try {
            // Obtener la actividad existente de BD.
            Actividad actividadExistente = actividadService.obtenerPorId(actividad.getId());
            if (actividadExistente == null) {
                attr.addFlashAttribute("error", "La actividad no existe o fue eliminada.");
                return "redirect:/admin/actividades";
            }

            // Actualizar los campos editables
            actividadExistente.setTitulo(actividad.getTitulo());
            actividadExistente.setDescripcion(actividad.getDescripcion());
            actividadExistente.setEnlace(actividad.getEnlace());

            if (imagenFile != null && !imagenFile.isEmpty()) {
               // Si ya tenía una imagen, eliminarla de Cloudinary
                if (actividadExistente.getImagenPublicId() != null) {
                    cloudinaryService.deleteFile(actividadExistente.getImagenPublicId());
                }

                // Subir nueva imagen
                Map<String, Object> uploadResult = cloudinaryService.uploadFile(imagenFile);
                actividadExistente.setImagen((String) uploadResult.get("secure_url"));
                actividadExistente.setImagenPublicId((String) uploadResult.get("public_id"));
            }

            // Guardar los cambios (actualizar)
            actividadService.guardar(actividadExistente);
            attr.addFlashAttribute("exito", "Actividad actualizada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al actualizar la actividad: " + e.getMessage());
        }

        return "redirect:/admin/actividades";
    }
    
    @GetMapping("/eliminar")
    public String eliminar(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            // Buscar la Actividad.
            Actividad actividad = actividadService.obtenerPorId(id);
            if (actividad == null) {
                attr.addFlashAttribute("error", "La actividad no existe o ya fue eliminado.");
                return "redirect:/admin/actividades";
            }
            // Eliminar la imágen asociada en Cloudinary (si existe).
            if (actividad.getImagenPublicId() != null && !actividad.getImagenPublicId().isEmpty()) {
                try {
                    cloudinaryService.deleteFile(actividad.getImagenPublicId());
                    System.out.println("✅ Imagen eliminada de Cloudinary: " + actividad.getImagenPublicId());
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo eliminar la imagen en Cloudinary: " + e.getMessage());
                }
            }
            // Eliminar el registro de la Base de Datos.
            actividadService.eliminarActividad(id);
            attr.addFlashAttribute("exito", "Actividad eliminado correctamente.");  

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "No se pudo eliminar la actividad: " + e.getMessage());
        }

        return "redirect:/admin/actividades";

    }

}
