package com.culturaweb.culturaya.model.entity;

import com.culturaweb.culturaya.model.enums.Rol;

import jakarta.persistence.*;
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

    private String nombre;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

}
