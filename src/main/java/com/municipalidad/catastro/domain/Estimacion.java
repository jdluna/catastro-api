package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estimacion", indexes = {
        @Index(name = "idx_estimacion_lote", columnList = "lote_id"),
        @Index(name = "idx_estimacion_codigo", columnList = "codigo_lote")
})
public class Estimacion extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id")
    public Lote lote;

    @Column(name = "codigo_lote", nullable = false, length = 8)
    public String codigoLote;

    @Column(name = "num_unidades_catastrales")
    public Integer numUnidadesCatastrales = 0;

    @Column(name = "tipo_terreno", length = 20)
    public String tipoTerreno; // "Sin Construir", "En Construcción", "Construido"

    @Column(name = "num_pisos")
    public Integer numPisos = 0;

    // Tipos de unidades
    @Column(name = "num_viviendas")
    public Integer numViviendas = 0;

    @Column(name = "num_comercios")
    public Integer numComercios = 0;

    @Column(name = "num_industrias")
    public Integer numIndustrias = 0;

    @Column(name = "num_educacion")
    public Integer numEducacion = 0;

    @Column(name = "num_salud")
    public Integer numSalud = 0;

    @Column(name = "num_religion")
    public Integer numReligion = 0;

    @Column(name = "num_estacionamientos")
    public Integer numEstacionamientos = 0;

    // Servicios contabilizados
    @Column(name = "num_medidores_luz")
    public Integer numMedidoresLuz = 0;

    @Column(name = "num_medidores_agua")
    public Integer numMedidoresAgua = 0;

    @Column(name = "num_timbres")
    public Integer numTimbres = 0;

    @Column(name = "num_sin_servicio")
    public Integer numSinServicio = 0;

    @Column(name = "observacion", length = 250)
    public String observacion;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = false)
    public LocalDateTime fechaModificacion;

    @PrePersist
    void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    // Método calculado para total de unidades catastrales
    @Transient
    public Integer calcularTotalUnidades() {
        return (numViviendas != null ? numViviendas : 0) +
                (numComercios != null ? numComercios : 0) +
                (numIndustrias != null ? numIndustrias : 0) +
                (numEducacion != null ? numEducacion : 0) +
                (numSalud != null ? numSalud : 0) +
                (numReligion != null ? numReligion : 0) +
                (numEstacionamientos != null ? numEstacionamientos : 0);
    }
}
