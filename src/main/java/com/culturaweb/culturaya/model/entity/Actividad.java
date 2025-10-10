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

    @NotBlank(message = "Título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String lugar;

    private String fecha;

    private String precio;

    @Size(max = 2000, message = "Descripción no debe superar los 2000 caracteres.")
    @Column(length = 2000)
    private String descripcion;

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
