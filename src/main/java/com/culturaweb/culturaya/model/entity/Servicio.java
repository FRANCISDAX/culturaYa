package com.culturaweb.culturaya.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título del Servicio.
    @NotBlank(message = "Título es obligatorio")
    @Size(min = 5, max = 100, message = "Título debe tener entre 5 y 100 caracteres")
    @Column(nullable=false, length = 100)    
    private String titulo;

    // Contenido o Descripción.
    @Size(max = 500, message = "Contenido no debe superar los 500 caracteres.")
    private String contenido;

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

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
