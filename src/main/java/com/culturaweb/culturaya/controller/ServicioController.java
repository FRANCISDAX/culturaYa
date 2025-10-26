package com.culturaweb.culturaya.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import com.culturaweb.culturaya.model.entity.Servicio;
import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.service.ServicioService;
import com.culturaweb.culturaya.service.CloudinaryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/servicios")
public class ServicioController {

    @Autowired
    ServicioService servicioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        if (usuario.getRol().name().equals("ADMIN")) {
            model.addAttribute("servicios", servicioService.listarServicios());
        } else {
            model.addAttribute("servicios", servicioService.listarPorUsuario(usuario.getId()));
        }

        model.addAttribute("servicio", new Servicio());
        model.addAttribute("vista", "adminServicios");
        return "privado/layout_admin";
    }

    @PostMapping("/guardar")
    public String guardar(
        @Valid Servicio servicio,
        BindingResult result,
        @RequestParam("imagenFile") MultipartFile imagenFile,
        RedirectAttributes attr) {
            
        if (result.hasErrors()) {
            result.getAllErrors().forEach(err -> System.out.println("Error: " + err.getDefaultMessage()));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("servicio", servicio);
            return "redirect:/admin/servicios";
        }

        try {
            // Validar si hay imágen subida.
            if (imagenFile == null || imagenFile.isEmpty()) {
                attr.addFlashAttribute("error", "Debe seleccionar una imagen.");
                return "redirect:/admin/servicios";
            }

            // Subir imagen a Cloudinary.
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(imagenFile);

            // Guardar la URL y el public_id.
            servicio.setImagen((String) uploadResult.get("secure_url"));
            servicio.setImagenPublicId((String) uploadResult.get("public_id"));

            // Guardar en la Base de Datos.
            servicioService.guardar(servicio);
            attr.addFlashAttribute("exito", "Servicio registrado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al guardar el servicio: " + e.getMessage());
        }

        return "redirect:/admin/servicios";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Servicio servicio = servicioService.obtenerPorId(id);

        if (servicio == null) {
            attr.addFlashAttribute("error", "El servicio no existe o fue eliminada.");
            return "redirect:/admin/servicios";
        }

        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("servicioEditar", servicio);
        model.addAttribute("vista", "adminServicios");
        return "privado/layout_admin";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @Valid Servicio servicio,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("servicio", servicio);
            return "redirect:/admin/servicios";
        }

        try {
            // Obtener el servicio existente de BD.
            Servicio servicioExistente = servicioService.obtenerPorId(servicio.getId());
            if (servicioExistente == null) {
                attr.addFlashAttribute("error", "El servicio no existe o fue eliminada.");
                return "redirect:/admin/servicios";
            }

            // Actualizar los campos editables
            servicioExistente.setTitulo(servicio.getTitulo());
            servicioExistente.setContenido(servicio.getContenido());
            servicioExistente.setEnlace(servicio.getEnlace());

            if (!imagenFile.isEmpty()) {
                if (servicioExistente.getImagenPublicId() != null) {
                    cloudinaryService.deleteFile(servicioExistente.getImagenPublicId());
                }

                Map<String, Object> uploadResult = cloudinaryService.uploadFile(imagenFile);

                servicioExistente.setImagen((String) uploadResult.get("secure_url"));
                servicioExistente.setImagenPublicId((String) uploadResult.get("public_id"));
            }

            // Guardar los cambios (actualizar).
            servicioService.guardar(servicioExistente);
            attr.addFlashAttribute("exito", "Servicio actualizado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al actualizar el servicio: " + e.getMessage());
        }

        return "redirect:/admin/servicios";
    }
    
    @GetMapping("/admins/servicios/eliminar")
    public String eliminar(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            // Buscar el Servicio.
            Servicio servicio = servicioService.obtenerPorId(id);
            if (servicio == null) {
                attr.addFlashAttribute("error", "El servicio no existe o ya fue eliminado.");
                return "redirect:/admin/servicios";
            }
            // Eliminar la imágen asociada en Cloudinary (si existe).
            if (servicio.getImagenPublicId() != null && !servicio.getImagenPublicId().isEmpty()) {
                try {
                    cloudinaryService.deleteFile(servicio.getImagenPublicId());
                    System.out.println("✅ Imagen eliminada de Cloudinary: " + servicio.getImagenPublicId());
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo eliminar la imagen en Cloudinary: " + e.getMessage());
                }
            }
            // Eliminar el registro de la Base de Datos.
            servicioService.eliminarServicio(id);
            attr.addFlashAttribute("exito", "Servicio eliminado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "No se pudo eliminar el servicio: " + e.getMessage());
        }

        return "redirect:/admin/servicios";

    }

}
