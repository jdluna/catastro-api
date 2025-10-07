package com.municipalidad.catastro.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ConstruccionDTO(
        Long id,
        Long fichaId,

        @Min(value = 0, message = "El número de piso debe ser mayor o igual a 0")
        Integer numeroPiso,

        String nombrePiso,
        LocalDate fechaConstruccion,

        @Min(value = 1900, message = "El año de construcción debe ser mayor a 1900")
        @Max(value = 2100, message = "El año de construcción debe ser menor a 2100")
        Integer anioConstruccion,

        String materialEstructural,
        String estadoConservacion,
        String estadoConstruccion,

        @DecimalMin(value = "0.0", message = "El área construida debe ser un valor positivo")
        BigDecimal areaConstruida,

        @DecimalMin(value = "0.0", message = "El área techada debe ser un valor positivo")
        BigDecimal areaTechada,

        @DecimalMin(value = "0.0", message = "El área común debe ser un valor positivo")
        BigDecimal areaComun,

        String muros,
        String techos,
        String pisos,
        String puertasVentanas,
        String revestimiento,
        String banios,
        String instalacionesSanitarias,
        String instalacionesElectricas,

        String categoriaMuro,
        String categoriaTecho,
        String categoriaPiso,
        String categoriaPuertaVentana,

        @Min(value = 0, message = "El número de habitaciones debe ser mayor o igual a 0")
        Integer numeroHabitaciones,

        @Min(value = 0, message = "El número de baños debe ser mayor o igual a 0")
        Integer numeroBanios,

        @Min(value = 0, message = "El número de cocinas debe ser mayor o igual a 0")
        Integer numeroCocinas,

        Boolean tieneGarage,
        Boolean tieneTerraza,
        Boolean tieneBalcon
) {}
