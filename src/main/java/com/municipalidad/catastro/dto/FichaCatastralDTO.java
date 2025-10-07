package com.municipalidad.catastro.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FichaCatastralDTO(
        Long id,

        @NotBlank(message = "El código del lote es requerido")
        @Pattern(regexp = "^\\d{8}$", message = "El código del lote debe tener exactamente 8 dígitos")
        String codigoLote,

        String codigoSector,
        String codigoManzana,
        String codigoUnidad,
        String codigoPiso,
        String codigoEdificacion,
        String codigoEntrada,
        Integer contadorFichas,

        String tipoPredio,
        String clasificacionPredio,
        String usoPredio,
        String predioCatastradoEn,

        // Domicilio fiscal
        String departamento,
        String provincia,
        String distrito,
        String zonaSectorEtapa,
        String manzana,
        String lote,
        String calleAvenida,
        String numeroMunicipal,
        String tipoInterior,
        String numeroInterior,
        String tipoPuerta,
        String numeroPuerta,
        String kilometro,
        String referenciaUbicacion,

        @DecimalMin(value = "0.0", message = "El frente debe ser un valor positivo")
        BigDecimal frenteMl,

        @DecimalMin(value = "0.0", message = "La derecha debe ser un valor positivo")
        BigDecimal derechaMl,

        @DecimalMin(value = "0.0", message = "La izquierda debe ser un valor positivo")
        BigDecimal izquierdaMl,

        @DecimalMin(value = "0.0", message = "El fondo debe ser un valor positivo")
        BigDecimal fondoMl,

        @DecimalMin(value = "0.0", message = "El área del terreno debe ser un valor positivo")
        BigDecimal areaTerreno,

        @DecimalMin(value = "0.0", message = "El área de construcción debe ser un valor positivo")
        BigDecimal areaConstruccion,

        @DecimalMin(value = "0.0", message = "El área verificada debe ser un valor positivo")
        BigDecimal areaVerificada,

        String linderoFrente,
        String linteroDerecha,
        String linderoIzquierda,
        String linderoFondo,

        String condicionNumeracion,
        String condicionPredio,

        LocalDate fechaLevantamiento,
        LocalDate fechaInscripcionRegistral,

        @Size(max = 5000, message = "Las observaciones no deben exceder los 5000 caracteres")
        String observaciones,

        List<TitularDTO> titulares,
        List<ConstruccionDTO> construcciones,
        ServicioDTO servicios
) {}
