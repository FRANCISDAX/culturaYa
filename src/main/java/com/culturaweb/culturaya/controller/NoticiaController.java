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
import com.culturaweb.culturaya.model.entity.Noticia;
import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.service.CloudinaryService;
import com.culturaweb.culturaya.service.NoticiaService;
import com.culturaweb.culturaya.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/noticias")
public class NoticiaController {

    @Autowired
    NoticiaService noticiaService;
    
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        if (usuario.getRol().name().equals("ADMIN")) {
            model.addAttribute("noticias", noticiaService.listarNoticias());
        } else {
            model.addAttribute("noticias", noticiaService.listarPorUsuario(usuario.getId()));
        }
        
        model.addAttribute("noticia", new Noticia()); 
        model.addAttribute("vista", "adminNoticias"); 
        return "privado/layout_admin";
    }

    @PostMapping("/guardar")
    public String guardar(
        @Valid Noticia noticia,
        BindingResult result,
        @RequestParam("imagenFile") MultipartFile imagenFile,
        RedirectAttributes attr) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if (result.hasErrors()) {
            result.getAllErrors().forEach(err -> System.out.println("Error: " + err.getDefaultMessage()));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("noticia", noticia);
            return "redirect:/admin/noticias";
        }

        try {
            // Validar si hay imágen subida.
            if (imagenFile == null || imagenFile.isEmpty()) {
                attr.addFlashAttribute("error", "Debe seleccionar una imagen.");
                return "redirect:/admin/noticias";
            }

            // Subir imagen a Cloudinary.
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(imagenFile);

            // Guardar la URL y el public_id.
            noticia.setImagen((String) uploadResult.get("secure_url"));
            noticia.setImagenPublicId((String) uploadResult.get("public_id"));

            // Guardar en la Base de Datos.
            noticia.setUsuario(usuario);
            noticiaService.guardar(noticia);
            attr.addFlashAttribute("exito", "Noticia registrado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al guardar la noticia: " + e.getMessage());
        }

        return "redirect:/admin/noticias";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Noticia noticia = noticiaService.obtenerPorId(id);

        if (noticia == null) {
            attr.addFlashAttribute("error", "La noticia no existe o fue eliminada.");
            return "redirect:/admin/noticias";
        }

        model.addAttribute("noticias", noticiaService.listarNoticias());
        model.addAttribute("noticiaEditar", noticia);
        model.addAttribute("vista", "adminNoticias");
        return "privado/layout_admin";
    }

    @PostMapping("/actualizar")
    public String actualizar(
            @Valid Noticia noticia,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("noticia", noticia);
            return "redirect:/admin/noticias";
        }

        try {
            // Obtener la noticia existente de BD.
            Noticia noticiaExistente = noticiaService.obtenerPorId(noticia.getId());
            if (noticiaExistente == null) {
                attr.addFlashAttribute("error", "La noticia no existe o fue eliminada.");
                return "redirect:/admin/noticias";
            }

            // Actualizar los campos editables
            noticiaExistente.setTitulo(noticia.getTitulo());
            noticiaExistente.setContenido(noticia.getContenido());
            noticiaExistente.setEnlace(noticia.getEnlace());

            if (!imagenFile.isEmpty()) {
                if (noticiaExistente.getImagenPublicId() != null) {
                    cloudinaryService.deleteFile(noticiaExistente.getImagenPublicId());
                }

                Map<String, Object> uploadResult = cloudinaryService.uploadFile(imagenFile);

                noticiaExistente.setImagen((String) uploadResult.get("secure_url"));
                noticiaExistente.setImagenPublicId((String) uploadResult.get("public_id"));
            }

            // Guardar los cambios (actualizar)
            noticiaService.guardar(noticiaExistente);
            attr.addFlashAttribute("exito", "Noticia actualizada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al actualizar la noticia: " + e.getMessage());
        }

        return "redirect:/admin/noticias";
    }
    
    @GetMapping("/eliminar")
    public String eliminar(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            // Buscar la Noticia.
            Noticia noticia = noticiaService.obtenerPorId(id);
            if (noticia == null) {
                attr.addFlashAttribute("error", "La noticia no existe o ya fue eliminado.");
                return "redirect:/admin/noticias";
            }
            // Eliminar la imágen asociada en Cloudinary (si existe).
            if (noticia.getImagenPublicId() != null && !noticia.getImagenPublicId().isEmpty()) {
                try {
                    cloudinaryService.deleteFile(noticia.getImagenPublicId());
                    System.out.println("✅ Imagen eliminada de Cloudinary: " + noticia.getImagenPublicId());
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo eliminar la imagen en Cloudinary: " + e.getMessage());
                }
            }
            // Eliminar el registro de la Base de Datos.
            noticiaService.eliminarNoticia(id);
            attr.addFlashAttribute("exito", "Noticia eliminado correctamente.");  

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "No se pudo eliminar la noticia: " + e.getMessage());
        }

        return "redirect:/admin/noticias";

    }

}
