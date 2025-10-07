package com.municipalidad.catastro.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoteDTO(
        Long id,

        @NotBlank(message = "El código del sector es requerido")
        @Size(min = 2, max = 2, message = "El código del sector debe tener exactamente 2 caracteres")
        String codigoSector,

        @NotBlank(message = "El código de la manzana es requerido")
        @Size(min = 3, max = 3, message = "El código de la manzana debe tener exactamente 3 caracteres")
        String codigoManzana,

        @NotBlank(message = "El código del lote es requerido")
        @Pattern(regexp = "^\\d{8}$", message = "El código del lote debe tener exactamente 8 dígitos")
        String codigoLote,

        @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
        @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
        BigDecimal latitud,

        @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
        @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
        BigDecimal longitud,

        @DecimalMin(value = "0.0", message = "La precisión debe ser un valor positivo")
        BigDecimal precisionMetros,

        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {}
