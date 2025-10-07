package com.municipalidad.catastro.dto;

public record ServicioDTO(
        Long id,
        Long fichaId,

        Boolean tieneLuz,
        String tipoLuz,

        Boolean tieneAgua,
        String tipoAgua,

        Boolean tieneDesague,
        String tipoDesague,

        Boolean tieneGas,
        String tipoGas,

        Boolean tieneTelefono,
        String tipoTelefono,

        Boolean tieneInternet,
        String tipoInternet,

        Boolean tieneTvCable,
        String operadorTv,

        Boolean viaPavimentada,
        Boolean viaAfirmada,
        Boolean viaTrocha,

        Boolean tieneTransportePublico,
        Integer distanciaTransporteMetros
) {}
