package com.culturaweb.culturaya.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.culturaweb.culturaya.model.entity.ImageConfig;
import com.culturaweb.culturaya.service.ImageConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/imagenes")
@RequiredArgsConstructor
public class ImageconfigController {

    
    private final ImageConfigService imageConfigService;

    @GetMapping
    public String listImages(Model model) {
        try {
            List<ImageConfig> images = imageConfigService.getAllImages();
            model.addAttribute("images", images);
            model.addAttribute("vista", "adminImagenes");
        } catch (Exception e) {
            log.error("Error al cargar imágenes", e);
            model.addAttribute("error", "Error al cargar las imágenes: " + e.getMessage());
        }
        return "admin/layout_admin";
    }

    
}
