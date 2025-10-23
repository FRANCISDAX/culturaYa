package com.culturaweb.culturaya.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.culturaweb.culturaya.model.entity.ImageConfig;
import com.culturaweb.culturaya.repository.ImageConfigRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageConfigService {

    @Autowired
    private ImageConfigRepository imageConfigRepository;
    
    private final Path rootLocation = Paths.get("uploads/images");
    
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            initializeDefaultSections();
        } catch (IOException e) {
            log.error("No se pudo crear el directorio de uploads", e);
            throw new RuntimeException("No se pudo crear el directorio de uploads", e);
        }
    }

    public List<ImageConfig> getAllImages() {
        return imageConfigRepository.findAll();
    }
    
    public Optional<ImageConfig> getBySection(String sectionName) {
        return imageConfigRepository.findBySectionName(sectionName);
    }
    
public ImageConfig saveOrUpdateImage(String sectionName, MultipartFile file, String displayName, String description) {
        try {
            // Validar parámetros requeridos
            if (sectionName == null || sectionName.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de sección no puede ser nulo o vacío");
            }
            
            Optional<ImageConfig> existingConfig = imageConfigRepository.findBySectionName(sectionName);
            ImageConfig imageConfig;
            
            if (existingConfig.isPresent()) {
                imageConfig = existingConfig.get();
                if (displayName != null) imageConfig.setDisplayName(displayName);
                if (description != null) imageConfig.setDescription(description);
            } else {
                // Crear nuevo config
                imageConfig = new ImageConfig(
                    sectionName,
                    displayName != null ? displayName : "Imagen de " + sectionName,
                    description != null ? description : "Imagen para la sección " + sectionName
                );
            }
            
            // Procesar archivo si se proporciona
            if (file != null && !file.isEmpty()) {
                processImageFile(file, imageConfig);
            }
            
            return imageConfigRepository.save(imageConfig);
            
        } catch (IOException e) {
            log.error("Error al guardar la imagen para la sección: {}", sectionName, e);
            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
        }
    }
    
    private void processImageFile(MultipartFile file, ImageConfig imageConfig) throws IOException {
        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }
        
        // Eliminar imagen anterior si existe
        if (imageConfig.getImagePath() != null) {
            try {
                Files.deleteIfExists(rootLocation.resolve(imageConfig.getImagePath()));
            } catch (IOException e) {
                log.warn("No se pudo eliminar la imagen anterior: {}", e.getMessage());
            }
        }
        
        // Guardar nueva imagen
        String filename = generateFilename(file.getOriginalFilename());
        Path destinationFile = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), destinationFile);
        
        imageConfig.setImagePath(filename);
        imageConfig.setImageUrl("/uploads/images/" + filename);
    }

    private String generateFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
    
    // Inicializar secciones por defecto
private void initializeDefaultSections() {
        log.info("Inicializando secciones por defecto...");
        
        String[][] sections = {
            {"inicio", "Imagen de Inicio", "Imagen principal de la página de inicio"},
            {"nosotros", "Imagen de Nosotros", "Imagen para la sección Nosotros"},
            {"agenda", "Imagen de Agenda", "Imagen para la sección Agenda"},
            {"servicios", "Imagen de Servicios", "Imagen para la sección Servicios"},
            {"contacto", "Imagen de Contacto", "Imagen para la sección Contacto"},
            {"header", "Imagen del Header", "Imagen del encabezado principal"},
            {"footer", "Imagen del Footer", "Imagen del pie de página"}
        };
        
        for (String[] section : sections) {
            try {
                String sectionName = section[0];
                
                if (!imageConfigRepository.existsBySectionName(sectionName)) {
                    ImageConfig config = new ImageConfig(sectionName, section[1], section[2]);
                    imageConfigRepository.save(config);
                    log.info("Sección creada: {}", sectionName);
                } else {
                    log.debug("Sección ya existe: {}", sectionName);
                }
                
            } catch (Exception e) {
                log.error("Error al crear sección {}: {}", section[0], e.getMessage());
            }
        }
        log.info("Inicialización de secciones completada");
    }
        
}
