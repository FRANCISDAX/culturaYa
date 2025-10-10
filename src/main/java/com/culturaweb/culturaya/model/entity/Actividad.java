package com.culturaweb.culturaya.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.culturaweb.culturaya.model.enums.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_actividades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título de la Actividad.
    @NotBlank(message = "Título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    // Categorías.
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    // Lugar donde se realizará.
    private String lugar;

    // Fecha donde se realizará.
    private String fecha;

    // Precio informativo.
    private String precio;

    // Contenido o Descripción.
    @Size(max = 2000, message = "Descripción no debe superar los 2000 caracteres.")
    @Column(length = 2000)
    private String descripcion;

    // URL pública de la imágen en Cloudinary.
    @Size(max = 200, message = "URL de la Imágen no debe superar los 200 caracteres.")
    @Column(length = 200)
    private String imagen;

    // ID interno de la imágen en Cloudinary (para eliminarla).
    @Column(length = 500)
    private String imagenPublicId;

    // Enlace externo.
    @NotBlank(message = "URL del Enlace es obligatorio")
    @Size(max = 200, message = "URL del Enlace no debe superar los 200 caracteres.")
    @Column(length = 200)
    private String enlace;

    // Fecha de Creación.
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaPublicacion;

    // Fecha de Actualización.
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

}
