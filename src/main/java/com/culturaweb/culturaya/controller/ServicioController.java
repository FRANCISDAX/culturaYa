package com.culturaweb.culturaya.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

import com.culturaweb.culturaya.model.entity.Servicio;
import com.culturaweb.culturaya.service.ServicioService;

import jakarta.validation.Valid;

@Controller
public class ServicioController {

    @Autowired
    ServicioService servicioService;

    @GetMapping("/admin/servicios")
    public String listar(Model model) {
        model.addAttribute("servicios",servicioService.listarServicios()); 
        return "privado/servicios/lista";
    }

    @GetMapping("/admin/servicios/nueva")
    public String nueva(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "privado/servicios/nueva";
    }

    @PostMapping("/admin/servicios/guardar")
    public String guardar(
        @Valid Servicio servicio,
        BindingResult result,
        @RequestParam("imagenFile") MultipartFile imagenFile,
        RedirectAttributes attr) {
            
        if (result.hasErrors()) {
            result.getAllErrors().forEach(err -> System.out.println("Error: " + err.getDefaultMessage()));
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("servicio", servicio);
            return "redirect:/admin/servicios/nueva";
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
                servicio.setImagen("/img/" + fileName);
            } else {
                attr.addFlashAttribute("error", "Debe seleccionar una imagen.");
                return "redirect:/admin/serviios/nueva";
            }

            // Guardar en BD
            servicioService.guardar(servicio);
            attr.addFlashAttribute("exito", "Servicio registrado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al guardar el servicio: " + e.getMessage());
        }

        return "redirect:/admin/servicios";

    }

    @GetMapping("/admin/servicios/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes attr) {
        Servicio servicio = servicioService.obtenerPorId(id);

        if (servicio == null) {
            attr.addFlashAttribute("error", "El servicio no existe o fue eliminada.");
            return "redirect:/admin/servicios";
        }

        model.addAttribute("servicio", servicio);
        return "privado/servicios/editar";
    }

    @PostMapping("/admin/servicios/actualizar")
    public String actualizar(
            @Valid Servicio servicio,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.noticia", result);
            attr.addFlashAttribute("servicio", servicio);
            return "redirect:/admin/servicios/editar/" + servicio.getId();
        }

        try {
            String uploadDir = "img_cultura/";

            // 🔹 Obtener la noticia existente de BD
            Servicio servicioExistente = servicioService.obtenerPorId(servicio.getId());
            if (servicioExistente == null) {
                attr.addFlashAttribute("error", "El servicio no existe o fue eliminada.");
                return "redirect:/admin/servicios";
            }

            // 🔹 Actualizar los campos editables
            servicioExistente.setTitulo(servicio.getTitulo());
            servicioExistente.setContenido(servicio.getContenido());
            servicioExistente.setEnlace(servicio.getEnlace());

            if (!imagenFile.isEmpty()) {
                // Eliminar la imagen anterior del disco (opcional)
                if (servicioExistente.getImagen() != null) {
                    Path rutaVieja = Paths.get("src/main/resources/static" + servicioExistente.getImagen());
                    Files.deleteIfExists(rutaVieja);
                }

                // Guardar la nueva imagen
                String fileName = imagenFile.getOriginalFilename();
                Path nuevaRuta = Paths.get(uploadDir + fileName);
                Files.createDirectories(nuevaRuta.getParent());
                Files.copy(imagenFile.getInputStream(), nuevaRuta, StandardCopyOption.REPLACE_EXISTING);

                servicioExistente.setImagen("/img/" + fileName);
            }

            // 🔹 Guardar los cambios (actualizar)
            servicioService.guardar(servicioExistente);
            attr.addFlashAttribute("exito", "Servicio actualizado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Error al actualizar el servicio: " + e.getMessage());
        }

        return "redirect:/admin/servicios";
    }
    
    @GetMapping("/admin/servicios/eliminar")
    public String eliminar(@RequestParam("id") Long id, RedirectAttributes attr) {
        try {
            servicioService.eliminarServicio(id);
            attr.addFlashAttribute("exito", "Servicio eliminado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "No se pudo eliminar el servicio: " + e.getMessage());
        }

        return "redirect:/admin/servicios";

    }

}
