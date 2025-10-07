package com.municipalidad.catastro.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TitularDTO(
        Long id,
        Long fichaId,

        @NotBlank(message = "El tipo de titular es requerido")
        String tipoTitular,

        String tipoDocumento,

        @NotBlank(message = "El número de documento es requerido")
        String numeroDocumento,

        String apellidoPaterno,
        String apellidoMaterno,
        String nombres,
        String razonSocial,
        String estadoCivil,
        String tipoPersonaJuridica,

        // Domicilio fiscal
        String domicilioDepartamento,
        String domicilioProvincia,
        String domicilioDistrito,
        String domicilioDireccion,

        @Pattern(regexp = "^[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
        String telefono,

        @Email(message = "El email debe tener un formato válido")
        String email,

        @DecimalMin(value = "0.0", message = "El porcentaje debe ser mayor o igual a 0")
        @DecimalMax(value = "100.0", message = "El porcentaje debe ser menor o igual a 100")
        BigDecimal porcentajePropiedad,

        String condicionTitular,
        String formaAdquisicion,
        LocalDate fechaAdquisicion,

        String tipoDocumentoLegal,
        String numeroPartida,
        String fojas,
        String asiento,
        LocalDate fechaInscripcion,
        String oficinaRegistral
) {}
