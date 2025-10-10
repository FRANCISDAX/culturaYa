package com.culturaweb.culturaya.controller;

import java.nio.file.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.culturaweb.culturaya.model.entity.Noticia;
import com.culturaweb.culturaya.service.NoticiaService;

import jakarta.validation.Valid;

@Controller
public class NoticiaController {

    @Autowired
    NoticiaService noticiaService;
    
    @GetMapping("/admin/noticias")
    public String listar(Model model) {
        model.addAttribute("noticias", noticiaService.listarNoticias());
        return "privado/noticias/lista";
    }

    @GetMapping("/admin/noticias/nueva")
    public String nueva(Model model) {
        model.addAttribute("noticia", new Noticia());
        return "privado/noticias/nueva";
    }

    @PostMapping("/admin/noticias/guardar")
    public String guardar(
        @Valid Noticia noticia,
        BindingResult result,
        @RequestParam("imagenFile") MultipartFile imagenFile,
        RedirectAttributes attr) {
            
        if (result.hasErrors()) {
            result.getAllErrors().forEach(err -> System.out.println("Error: " + err.getDefaultMessage()));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("noticia", noticia);
            return "redirect:/admin/noticias/nueva";
        }

        try {
            // Ruta donde se guardarán las imágenes
            String uploadDir = "img_cultura/";

            // Validar si hay imagen subida
            if (!imagenFile.isEmpty()) {
                String fileName = imagenFile.getOriginalFilename();

                // Guardar el archivo físicamente
                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
                java.nio.file.Files.createDirectories(path.getParent()); // Crea la carpeta si no existe
                java.nio.file.Files.copy(imagenFile.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Asignar la ruta pública al objeto
                noticia.setImagen("/img/" + fileName);
            } else {
                attr.addFlashAttribute("error", "Debe seleccionar una imagen.");
                return "redirect:/admin/noticias/nueva";
            }

            // Guardar en BD
            noticiaService.guardar(noticia);
            attr.addFlashAttribute("exito", "Noticia registrada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al guardar la noticia: " + e.getMessage());
        }

        return "redirect:/admin/noticias";

    }

    @GetMapping("/admin/noticias/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Noticia noticia = noticiaService.obtenerPorId(id);

        if (noticia == null) {
            attr.addFlashAttribute("error", "La noticia no existe o fue eliminada.");
            return "redirect:/admin/noticias";
        }

        model.addAttribute("noticia", noticia);
        return "privado/noticias/editar";
    }

    @PostMapping("/admin/noticias/actualizar")
    public String actualizar(
            @Valid Noticia noticia,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("noticia", noticia);
            return "redirect:/admin/noticias/editar/" + noticia.getId();
        }

        try {
            String uploadDir = "img_cultura/";

            // 🔹 Obtener la noticia existente de BD
            Noticia noticiaExistente = noticiaService.obtenerPorId(noticia.getId());
            if (noticiaExistente == null) {
                attr.addFlashAttribute("error", "La noticia no existe o fue eliminada.");
                return "redirect:/admin/noticias";
            }

            // 🔹 Actualizar los campos editables
            noticiaExistente.setTitulo(noticia.getTitulo());
            noticiaExistente.setContenido(noticia.getContenido());
            noticiaExistente.setEnlace(noticia.getEnlace());

            if (!imagenFile.isEmpty()) {
                // Eliminar la imagen anterior del disco (opcional)
                if (noticiaExistente.getImagen() != null) {
                    Path rutaVieja = Paths.get("src/main/resources/static" + noticiaExistente.getImagen());
                    Files.deleteIfExists(rutaVieja);
                }

                // Guardar la nueva imagen
                String fileName = imagenFile.getOriginalFilename();
                Path nuevaRuta = Paths.get(uploadDir + fileName);
                Files.createDirectories(nuevaRuta.getParent());
                Files.copy(imagenFile.getInputStream(), nuevaRuta, StandardCopyOption.REPLACE_EXISTING);

                noticiaExistente.setImagen("/img/" + fileName);
            }

            // 🔹 Guardar los cambios (actualizar)
            noticiaService.guardar(noticiaExistente);
            attr.addFlashAttribute("exito", "Noticia actualizada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al actualizar la noticia: " + e.getMessage());
        }

        return "redirect:/admin/noticias";
    }
    
    @GetMapping("/admin/noticias/eliminar")
    public String eliminar(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            noticiaService.eliminarNoticia(id);
            attr.addFlashAttribute("exito", "Noticia eliminada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "No se pudo eliminar la noticia: " + e.getMessage());
        }

        return "redirect:/admin/noticias";

    }

}
