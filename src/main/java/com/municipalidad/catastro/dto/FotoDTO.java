package com.municipalidad.catastro.dto;

import jakarta.validation.constraints.*;

public record FotoDTO(
        Long id,
        Long loteId,

        @NotBlank(message = "El código del lote es requerido")
        @Pattern(regexp = "^\\d{8}$", message = "El código del lote debe tener exactamente 8 dígitos")
        String codigoLote,

        String servicio,

        @NotBlank(message = "El nombre del archivo es requerido")
        @Size(max = 100, message = "El nombre del archivo no debe exceder los 100 caracteres")
        String nombre,

        @NotBlank(message = "La URL es requerida")
        String url,

        String tipoTerreno,
        String tipoFoto,
        String contentType,
        Long tamanioBytes
) {}
