package com.culturaweb.culturaya.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    /**
     * Sube una imagen a Cloudinary
     * Retorna un Map con datos como:
     * - secure_url (URL pública)
     * - public_id (para eliminarla luego)
     */
    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("El archivo está vacío o es nulo");
        }

        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "culturaya",
                        "resource_type", "auto"
                ));
    }

    /**
     * Elimina una imagen en Cloudinary usando su public_id
     */
    public void deleteFile(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            return;
        }

        Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        // Si Cloudinary devuelve "not found", se ignora sin error.
        if (!"ok".equals(result.get("result"))) {
            System.out.println("⚠️ Imagen no encontrada o ya eliminada: " + publicId);
        }
    }

}
