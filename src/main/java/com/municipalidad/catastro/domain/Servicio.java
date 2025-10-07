package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicio", indexes = {
        @Index(name = "idx_servicio_ficha", columnList = "ficha_id")
})
public class Servicio extends PanacheEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id", unique = true)
    public FichaCatastral ficha;

    // Servicios básicos
    @Column(name = "tiene_luz")
    public Boolean tieneLuz = false;

    @Column(name = "tipo_luz", length = 50)
    public String tipoLuz; // "Red Pública", "Generador", "Panel Solar"

    @Column(name = "tiene_agua")
    public Boolean tieneAgua = false;

    @Column(name = "tipo_agua", length = 50)
    public String tipoAgua; // "Red Pública", "Pozo", "Cisterna", "Camión"

    @Column(name = "tiene_desague")
    public Boolean tieneDesague = false;

    @Column(name = "tipo_desague", length = 50)
    public String tipoDesague; // "Red Pública", "Pozo Séptico", "Pozo Ciego"

    // Servicios adicionales
    @Column(name = "tiene_gas")
    public Boolean tieneGas = false;

    @Column(name = "tipo_gas", length = 50)
    public String tipoGas; // "Red Pública", "Balón GLP"

    @Column(name = "tiene_telefono")
    public Boolean tieneTelefono = false;

    @Column(name = "tipo_telefono", length = 50)
    public String tipoTelefono; // "Fijo", "Móvil"

    @Column(name = "tiene_internet")
    public Boolean tieneInternet = false;

    @Column(name = "tipo_internet", length = 50)
    public String tipoInternet; // "Fibra Óptica", "Cable", "Inalámbrico"

    @Column(name = "tiene_tv_cable")
    public Boolean tieneTvCable = false;

    @Column(name = "operador_tv", length = 50)
    public String operadorTv;

    // Vías de acceso
    @Column(name = "via_pavimentada")
    public Boolean viaPavimentada = false;

    @Column(name = "via_afirmada")
    public Boolean viaAfirmada = false;

    @Column(name = "via_trocha")
    public Boolean viaTrocha = false;

    // Transporte
    @Column(name = "tiene_transporte_publico")
    public Boolean tieneTransportePublico = false;

    @Column(name = "distancia_transporte_metros")
    public Integer distanciaTransporteMetros;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @PrePersist
    void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @Transient
    public int contarServiciosBasicos() {
        int count = 0;
        if (Boolean.TRUE.equals(tieneLuz)) count++;
        if (Boolean.TRUE.equals(tieneAgua)) count++;
        if (Boolean.TRUE.equals(tieneDesague)) count++;
        return count;
    }
}
