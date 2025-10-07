package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "construccion", indexes = {
        @Index(name = "idx_construccion_ficha", columnList = "ficha_id"),
        @Index(name = "idx_construccion_piso", columnList = "numero_piso")
})
public class Construccion extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id")
    public FichaCatastral ficha;

    @Column(name = "numero_piso")
    public Integer numeroPiso;

    @Column(name = "nombre_piso", length = 50)
    public String nombrePiso; // "Sótano", "Semisótano", "Piso 1", "Azotea"

    @Column(name = "fecha_construccion")
    public LocalDate fechaConstruccion;

    @Column(name = "anio_construccion")
    public Integer anioConstruccion;

    // Características estructurales
    @Column(name = "material_estructural", length = 50)
    public String materialEstructural; // "Concreto", "Ladrillo", "Adobe", "Madera"

    @Column(name = "estado_conservacion", length = 20)
    public String estadoConservacion; // "Muy Bueno", "Bueno", "Regular", "Malo", "Muy Malo"

    @Column(name = "estado_construccion", length = 20)
    public String estadoConstruccion; // "Terminado", "En Construcción", "Inconcluso"

    // Áreas
    @Column(name = "area_construida", precision = 10, scale = 2)
    public BigDecimal areaConstruida;

    @Column(name = "area_techada", precision = 10, scale = 2)
    public BigDecimal areaTechada;

    @Column(name = "area_comun", precision = 10, scale = 2)
    public BigDecimal areaComun;

    // Elementos constructivos (Escala de calidad: A, B, C, D)
    @Column(name = "muros", length = 10)
    public String muros;

    @Column(name = "techos", length = 10)
    public String techos;

    @Column(name = "pisos", length = 10)
    public String pisos;

    @Column(name = "puertas_ventanas", length = 10)
    public String puertasVentanas;

    @Column(name = "revestimiento", length = 10)
    public String revestimiento;

    @Column(name = "banios", length = 10)
    public String banios;

    @Column(name = "instalaciones_sanitarias", length = 10)
    public String instalacionesSanitarias;

    @Column(name = "instalaciones_electricas", length = 10)
    public String instalacionesElectricas;

    // Categorías según materiales
    @Column(name = "categoria_muro", length = 50)
    public String categoriaMuro; // "Ladrillo o similar", "Adobe", "Madera", "Quincha"

    @Column(name = "categoria_techo", length = 50)
    public String categoriaTecho; // "Concreto Armado", "Calamina", "Teja", "Paja"

    @Column(name = "categoria_piso", length = 50)
    public String categoriaPiso; // "Parquet", "Cemento", "Tierra", "Cerámico"

    @Column(name = "categoria_puerta_ventana", length = 50)
    public String categoriaPuertaVentana; // "Madera", "Metal", "Aluminio"

    // Distribución
    @Column(name = "numero_habitaciones")
    public Integer numeroHabitaciones;

    @Column(name = "numero_banios")
    public Integer numeroBanios;

    @Column(name = "numero_cocinas")
    public Integer numeroCocinas;

    @Column(name = "tiene_garage")
    public Boolean tieneGarage = false;

    @Column(name = "tiene_terraza")
    public Boolean tieneTerraza = false;

    @Column(name = "tiene_balcon")
    public Boolean tieneBalcon = false;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @PrePersist
    void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
