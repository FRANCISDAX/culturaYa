package com.culturaweb.culturaya.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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

import com.culturaweb.culturaya.model.entity.Actividad;
import com.culturaweb.culturaya.model.enums.Categoria;
import com.culturaweb.culturaya.service.ActividadService;

import jakarta.validation.Valid;

@Controller
public class ActividadController {

    @Autowired
    ActividadService actividadService;

    @GetMapping("/admin/actividades")
    public String listar(@RequestParam(required = false) String categoria, 
        Model model) {
        
        List<Actividad> actividades;

        actividades = actividadService.buscarPorCategoria(categoria);

        model.addAttribute("actividades", actividades);
        model.addAttribute("categoriaSeleccionada", categoria);
        return "privado/actividades/lista";
    }

    @GetMapping("/admin/actividades/nueva")
    public String nueva(Model model) {
        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("actividades", new Actividad());
        return "privado/actividades/nueva";
    }

    @PostMapping("/admin/actividades/guardar")
    public String guardar(
        @Valid Actividad actividad,
        BindingResult result,
        @RequestParam("imagenFile") MultipartFile imagenFile,
        RedirectAttributes attr) {
            
        if (result.hasErrors()) {
            result.getAllErrors().forEach(err -> System.out.println("Error: " + err.getDefaultMessage()));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.actividad", result);
            attr.addFlashAttribute("actividad", actividad);
            return "redirect:/admin/actividades/nueva";
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
                actividad.setImagen("/img/" + fileName);
            } else {
                attr.addFlashAttribute("error", "Debe seleccionar una imagen.");
                return "redirect:/admin/actividades/nueva";
            }

            // Guardar en BD
            actividadService.guardar(actividad);
            attr.addFlashAttribute("exito", "Actividad registrada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al guardar la actividad: " + e.getMessage());
        }

        return "redirect:/admin/actividades";

    }

    @GetMapping("/admin/actividades/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Actividad actividad = actividadService.obtenerPorId(id);

        if (actividad == null) {
            attr.addFlashAttribute("error", "La actividad no existe o fue eliminada.");
            return "redirect:/admin/actividades";
        }

        model.addAttribute("actividad", actividad);
        return "privado/actividades/editar";
    }

    @PostMapping("/admin/actividades/actualizar")
    public String actualizar(
            @Valid Actividad actividad,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("actividad", actividad);
            return "redirect:/admin/actividades/editar/" + actividad.getId();
        }

        try {
            String uploadDir = "img_cultura/";

            // 🔹 Obtener la noticia existente de BD
            Actividad actividadExistente = actividadService.obtenerPorId(actividad.getId());
            if (actividadExistente == null) {
                attr.addFlashAttribute("error", "La actividad no existe o fue eliminada.");
                return "redirect:/admin/actividades";
            }

            // 🔹 Actualizar los campos editables
            actividadExistente.setTitulo(actividad.getTitulo());
            actividadExistente.setDescripcion(actividad.getDescripcion());
            actividadExistente.setEnlace(actividad.getEnlace());

            if (!imagenFile.isEmpty()) {
                // Eliminar la imagen anterior del disco (opcional)
                if (actividadExistente.getImagen() != null) {
                    Path rutaVieja = Paths.get("src/main/resources/static" + actividadExistente.getImagen());
                    Files.deleteIfExists(rutaVieja);
                }

                // Guardar la nueva imagen
                String fileName = imagenFile.getOriginalFilename();
                Path nuevaRuta = Paths.get(uploadDir + fileName);
                Files.createDirectories(nuevaRuta.getParent());
                Files.copy(imagenFile.getInputStream(), nuevaRuta, StandardCopyOption.REPLACE_EXISTING);

                actividadExistente.setImagen("/img/" + fileName);
            }

            // 🔹 Guardar los cambios (actualizar)
            actividadService.guardar(actividadExistente);
            attr.addFlashAttribute("exito", "Actividad actualizada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al actualizar la actividad: " + e.getMessage());
        }

        return "redirect:/admin/actividades";
    }
    
    @GetMapping("/admin/actividades/eliminar")
    public String eliminar(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            actividadService.eliminarActividad(id);
            attr.addFlashAttribute("exito", "Actividad eliminada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "No se pudo eliminar la actividad: " + e.getMessage());
        }

        return "redirect:/admin/actividades";

    }


}
