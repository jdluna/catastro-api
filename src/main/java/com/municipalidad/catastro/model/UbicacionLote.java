package com.municipalidad.catastro.model;

public record UbicacionLote(
    String latitud,
    String longitud,
    String codigoSectorCatastral,
    String codigoManzanaCatastral,
    String departamento,
    String provincia,
    String distrito
) {}
