package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "foto", indexes = {
        @Index(name = "idx_foto_lote", columnList = "lote_id"),
        @Index(name = "idx_foto_codigo", columnList = "codigo_lote"),
        @Index(name = "idx_foto_url", columnList = "url")
})
public class Foto extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    public Lote lote;

    @Column(name = "codigo_lote", nullable = false, length = 8)
    public String codigoLote;

    @Column(name = "servicio", length = 20)
    public String servicio = "S3";

    @Column(name = "nombre", nullable = false, length = 100)
    public String nombre;

    @Column(name = "url", nullable = false)
    public String url;

    @Column(name = "tipo_terreno", length = 20)
    public String tipoTerreno; // Relación con el tipo de terreno fotografiado

    @Column(name = "tipo_foto", length = 50)
    public String tipoFoto; // "Fachada", "Interior", "Plano", "Documento"

    @Column(name = "content_type", length = 50)
    public String contentType; // "image/jpeg", "image/png", "application/pdf"

    @Column(name = "tamanio_bytes")
    public Long tamanioBytes;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @PrePersist
    void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
