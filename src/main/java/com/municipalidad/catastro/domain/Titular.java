package com.municipalidad.catastro.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "titular", indexes = {
        @Index(name = "idx_titular_ficha", columnList = "ficha_id"),
        @Index(name = "idx_titular_documento", columnList = "numero_documento")
})
public class Titular extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id")
    public FichaCatastral ficha;

    // Tipo de titular
    @Column(name = "tipo_titular", length = 20)
    public String tipoTitular; // "1" = Persona Natural, "2" = Persona Jurídica

    // Tipo de documento
    @Column(name = "tipo_documento", length = 20)
    public String tipoDocumento; // "DNI", "RUC", "Pasaporte", "Carnet Extranjería"

    @Column(name = "numero_documento", length = 20)
    public String numeroDocumento;

    // Para persona natural
    @Column(name = "apellido_paterno", length = 100)
    public String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    public String apellidoMaterno;

    @Column(name = "nombres", length = 100)
    public String nombres;

    @Column(name = "estado_civil", length = 20)
    public String estadoCivil; // "Soltero", "Casado", "Divorciado", "Viudo", "Conviviente"

    // Para persona jurídica
    @Column(name = "razon_social", length = 200)
    public String razonSocial;

    @Column(name = "tipo_persona_juridica", length = 50)
    public String tipoPersonaJuridica; // "SAC", "SRL", "SA", "Asociación"

    // Domicilio fiscal del titular
    @Column(name = "domicilio_departamento", length = 50)
    public String domicilioDepartamento;

    @Column(name = "domicilio_provincia", length = 50)
    public String domicilioProvincia;

    @Column(name = "domicilio_distrito", length = 50)
    public String domicilioDistrito;

    @Column(name = "domicilio_direccion", length = 255)
    public String domicilioDireccion;

    @Column(name = "telefono", length = 20)
    public String telefono;

    @Column(name = "email", length = 100)
    public String email;

    // Propiedad
    @Column(name = "porcentaje_propiedad", precision = 5, scale = 2)
    public BigDecimal porcentajePropiedad;

    @Column(name = "condicion_titular", length = 50)
    public String condicionTitular; // "Propietario Único", "Copropietario", "Poseedor"

    // Forma de adquisición
    @Column(name = "forma_adquisicion", length = 50)
    public String formaAdquisicion; // "Compra", "Herencia", "Donación", "Adjudicación"

    @Column(name = "fecha_adquisicion")
    public LocalDate fechaAdquisicion;

    // Documento de propiedad
    @Column(name = "tipo_documento_legal", length = 50)
    public String tipoDocumentoLegal; // "Escritura Pública", "Minuta", "Declaratoria de Herederos"

    @Column(name = "numero_partida", length = 50)
    public String numeroPartida;

    @Column(name = "fojas", length = 20)
    public String fojas;

    @Column(name = "asiento", length = 20)
    public String asiento;

    @Column(name = "fecha_inscripcion")
    public LocalDate fechaInscripcion;

    @Column(name = "oficina_registral", length = 100)
    public String oficinaRegistral;

    @Column(name = "fecha_creacion", nullable = false)
    public LocalDateTime fechaCreacion;

    @PrePersist
    void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @Transient
    public String getNombreCompleto() {
        if (tipoTitular != null && tipoTitular.equals("2")) {
            return razonSocial;
        }
        return String.format("%s %s, %s",
                apellidoPaterno != null ? apellidoPaterno : "",
                apellidoMaterno != null ? apellidoMaterno : "",
                nombres != null ? nombres : "").trim();
    }
}
