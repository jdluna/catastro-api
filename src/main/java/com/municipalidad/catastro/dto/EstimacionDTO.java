package com.municipalidad.catastro.dto;

import jakarta.validation.constraints.*;

public record EstimacionDTO(
        Long id,

        @Min(value = 0, message = "El número de unidades catastrales debe ser mayor o igual a 0")
        Integer numUnidadesCatastrales,

        String tipoTerreno,

        @Min(value = 0, message = "El número de pisos debe ser mayor o igual a 0")
        Integer numPisos,

        @Min(value = 0, message = "El número de viviendas debe ser mayor o igual a 0")
        Integer numViviendas,

        @Min(value = 0, message = "El número de comercios debe ser mayor o igual a 0")
        Integer numComercios,

        @Min(value = 0, message = "El número de industrias debe ser mayor o igual a 0")
        Integer numIndustrias,

        @Min(value = 0, message = "El número de edificaciones educativas debe ser mayor o igual a 0")
        Integer numEducacion,

        @Min(value = 0, message = "El número de edificaciones de salud debe ser mayor o igual a 0")
        Integer numSalud,

        @Min(value = 0, message = "El número de edificaciones religiosas debe ser mayor o igual a 0")
        Integer numReligion,

        @Min(value = 0, message = "El número de estacionamientos debe ser mayor o igual a 0")
        Integer numEstacionamientos,

        @Min(value = 0, message = "El número de medidores de luz debe ser mayor o igual a 0")
        Integer numMedidoresLuz,

        @Min(value = 0, message = "El número de medidores de agua debe ser mayor o igual a 0")
        Integer numMedidoresAgua,

        @Min(value = 0, message = "El número de timbres debe ser mayor o igual a 0")
        Integer numTimbres,

        @Min(value = 0, message = "El número de unidades sin servicio debe ser mayor o igual a 0")
        Integer numSinServicio,

        @Size(max = 250, message = "La observación no debe exceder los 250 caracteres")
        String observacion
) {}
