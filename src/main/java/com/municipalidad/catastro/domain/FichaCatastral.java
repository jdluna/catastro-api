package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ficha_catastral", indexes = {
        @Index(name = "idx_ficha_codigo", columnList = "codigo_lote"),
        @Index(name = "idx_ficha_sector", columnList = "codigo_sector"),
        @Index(name = "idx_ficha_tipo", columnList = "tipo_predio")
})
public class FichaCatastral extends PanacheEntity {

    // Códigos catastrales
    @Column(name = "codigo_lote", nullable = false, length = 8)
    public String codigoLote;

    @Column(name = "codigo_sector", length = 2)
    public String codigoSector;

    @Column(name = "codigo_manzana", length = 3)
    public String codigoManzana;

    @Column(name = "codigo_unidad", length = 3)
    public String codigoUnidad;

    @Column(name = "codigo_piso", length = 2)
    public String codigoPiso;

    @Column(name = "codigo_edificacion", length = 2)
    public String codigoEdificacion;

    @Column(name = "codigo_entrada", length = 2)
    public String codigoEntrada;

    @Column(name = "contador_fichas")
    public Integer contadorFichas;

    // Clasificación del predio
    @Column(name = "tipo_predio", length = 50)
    public String tipoPredio; // "Casa Habitación", "Edificio", "Tienda", etc.

    @Column(name = "clasificacion_predio", length = 50)
    public String clasificacionPredio; // "Urbano", "Rural"

    @Column(name = "uso_predio", length = 50)
    public String usoPredio; // "Residencial", "Comercial", "Industrial"

    @Column(name = "predio_catastrado_en", length = 20)
    public String predioCatastradoEn; // "Rural", "Urbano"

    // Domicilio fiscal
    @Column(name = "departamento", length = 50)
    public String departamento;

    @Column(name = "provincia", length = 50)
    public String provincia;

    @Column(name = "distrito", length = 50)
    public String distrito;

    @Column(name = "zona_sector_etapa", length = 100)
    public String zonaSectorEtapa;

    @Column(name = "manzana", length = 20)
    public String manzana;

    @Column(name = "lote", length = 20)
    public String lote;

    @Column(name = "calle_avenida", length = 150)
    public String calleAvenida;

    @Column(name = "numero_municipal", length = 20)
    public String numeroMunicipal;

    @Column(name = "tipo_interior", length = 20)
    public String tipoInterior;

    @Column(name = "numero_interior", length = 20)
    public String numeroInterior;

    @Column(name = "tipo_puerta", length = 20)
    public String tipoPuerta;

    @Column(name = "numero_puerta", length = 20)
    public String numeroPuerta;

    @Column(name = "kilometro", length = 20)
    public String kilometro;

    @Column(name = "referencia_ubicacion", length = 255)
    public String referenciaUbicacion;

    // Medidas perimétricas (en metros lineales)
    @Column(name = "frente_ml", precision = 10, scale = 2)
    public BigDecimal frenteMl;

    @Column(name = "derecha_ml", precision = 10, scale = 2)
    public BigDecimal derechaMl;

    @Column(name = "izquierda_ml", precision = 10, scale = 2)
    public BigDecimal izquierdaMl;

    @Column(name = "fondo_ml", precision = 10, scale = 2)
    public BigDecimal fondoMl;

    // Áreas
    @Column(name = "area_terreno", precision = 10, scale = 2)
    public BigDecimal areaTerreno;

    @Column(name = "area_construccion", precision = 10, scale = 2)
    public BigDecimal areaConstruccion;

    @Column(name = "area_verificada", precision = 10, scale = 2)
    public BigDecimal areaVerificada;

    // Linderos y colindancias
    @Column(name = "lindero_frente", length = 255)
    public String linderoFrente;

    @Column(name = "lindero_derecha", length = 255)
    public String linteroDerecha;

    @Column(name = "lindero_izquierda", length = 255)
    public String linderoIzquierda;

    @Column(name = "lindero_fondo", length = 255)
    public String linderoFondo;

    // Características específicas
    @Column(name = "condicion_numeracion", length = 50)
    public String condicionNumeracion; // "Existe", "No existe"

    @Column(name = "condicion_predio", length = 50)
    public String condicionPredio; // "Propio", "Alquilado", "Litigio"

    // Fechas
    @Column(name = "fecha_levantamiento")
    public LocalDate fechaLevantamiento;

    @Column(name = "fecha_inscripcion_registral")
    public LocalDate fechaInscripcionRegistral;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    public String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = false)
    public LocalDateTime fechaModificacion;

    // Relaciones
    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Titular> titulares = new ArrayList<>();

    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<Construccion> construcciones = new ArrayList<>();

    @OneToOne(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public Servicio servicios;

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
    public void addTitular(Titular titular) {
        titulares.add(titular);
        titular.ficha = this;
    }

    public void removeTitular(Titular titular) {
        titulares.remove(titular);
        titular.ficha = null;
    }

    public void addConstruccion(Construccion construccion) {
        construcciones.add(construccion);
        construccion.ficha = this;
    }

    public void removeConstruccion(Construccion construccion) {
        construcciones.remove(construccion);
        construccion.ficha = null;
    }

    @Transient
    public BigDecimal calcularAreaTotalConstruccion() {
        return construcciones.stream()
                .map(c -> c.areaConstruida != null ? c.areaConstruida : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
