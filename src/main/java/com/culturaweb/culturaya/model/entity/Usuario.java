package com.culturaweb.culturaya.model.entity;

import com.culturaweb.culturaya.model.enums.Rol;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tbl_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres.")    
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;

    @Builder.Default
    private boolean estado = true;

    @Enumerated(EnumType.STRING)
    private Rol rol;

}
