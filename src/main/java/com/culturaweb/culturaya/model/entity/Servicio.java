package com.culturaweb.culturaya.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tbl_servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título es obligatorio")
    @Size(min = 5, max = 100, message = "Título debe tener entre 5 y 100 caracteres")
    @Column(nullable=false, length = 100)    
    private String titulo;

    @Size(max = 500, message = "Contenido no debe superar los 500 caracteres.")
    private String contenido;

    @Size(max = 200, message = "URL de la Imágen no debe superar los 200 caracteres.")
    @Column(length = 200)
    private String imagen;

    @NotBlank(message = "URL del Enlace es obligatorio")
    @Size(max = 200, message = "URL del Enlace no debe superar los 200 caracteres.")
    @Column(length = 200)
    private String enlace;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaPublicacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

}
