package com.culturaweb.culturaya.model.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_movimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entidad;     // Ejemplo: "Noticia", "Actividad", "Servicio"
    private String accion;      // Ejemplo: "Creado", "Actualizado", "Eliminado"
    private String descripcion; // Opcional, algo como "Se actualizó la noticia: Cultura 2025"

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public Movimiento(String entidad, String accion, String descripcion) {
        this.entidad = entidad;
        this.accion = accion;
        this.descripcion = descripcion;
        this.fecha = new Date();
    }
    
}
