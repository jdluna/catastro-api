package com.municipalidad.catastro.model;

public record DatosLote(
    String tipoLote,
    Integer numPisos,
    Integer numViviendas,
    Integer numComercios,
    Integer numIndustrias,
    Integer numCentrosEducacion,
    String observacion
) {}
