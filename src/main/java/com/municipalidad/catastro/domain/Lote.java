package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lote", indexes = {
        @Index(name = "idx_lote_codigo", columnList = "codigo_lote"),
        @Index(name = "idx_lote_sector", columnList = "codigo_sector"),
        @Index(name = "idx_lote_manzana", columnList = "codigo_manzana")
})
public class Lote extends PanacheEntity {

    @Column(name = "codigo_sector", nullable = false, length = 2)
    public String codigoSector;

    @Column(name = "codigo_manzana", nullable = false, length = 3)
    public String codigoManzana;

    @Column(name = "codigo_lote", unique = true, nullable = false, length = 8)
    public String codigoLote;

    @Column(name = "latitud", precision = 10, scale = 8)
    public BigDecimal latitud;

    @Column(name = "longitud", precision = 11, scale = 8)
    public BigDecimal longitud;

    @Column(name = "precision_metros", precision = 5, scale = 2)
    public BigDecimal precisionMetros;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = false)
    public LocalDateTime fechaModificacion;

    // Relaciones
    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Estimacion> estimaciones = new ArrayList<>();

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Foto> fotos = new ArrayList<>();

    @PrePersist
    void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    // Métodos de utilidad
    public void addEstimacion(Estimacion estimacion) {
        estimaciones.add(estimacion);
        estimacion.lote = this;
    }

    public void removeEstimacion(Estimacion estimacion) {
        estimaciones.remove(estimacion);
        estimacion.lote = null;
    }

    public void addFoto(Foto foto) {
        fotos.add(foto);
        foto.lote = this;
    }

    public void removeFoto(Foto foto) {
        fotos.remove(foto);
        foto.lote = null;
    }
}
