package com.municipalidad.catastro.model;

import java.time.LocalDateTime;

public record Estimacion(
    Integer id,
    Integer loteId,
    String codigoLote,
    Integer numUnidadesCatastrales,
    String tipoTerreno,
    Integer numViviendas,
    Integer numComercios,
    Integer numIndustrias,
    Integer numEducacion,
    Integer numSalud,
    Integer numReligion,
    Integer numEstacionamientos,
    String observacion,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaModificacion
) {
    public Estimacion withId(Integer newId) {
        return new Estimacion(newId, loteId, codigoLote, numUnidadesCatastrales,
                tipoTerreno, numViviendas, numComercios, numIndustrias,
                numEducacion, numSalud, numReligion, numEstacionamientos,
                observacion, fechaCreacion, fechaModificacion);
    }

    public Estimacion withTimestamps(LocalDateTime now) {
        return new Estimacion(id, loteId, codigoLote, numUnidadesCatastrales,
                tipoTerreno, numViviendas, numComercios, numIndustrias,
                numEducacion, numSalud, numReligion, numEstacionamientos,
                observacion, now, now);
    }
}
