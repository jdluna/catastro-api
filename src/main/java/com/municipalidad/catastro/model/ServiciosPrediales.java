package com.municipalidad.catastro.model;

public record ServiciosPrediales(
    Integer numMedidoresLuz,
    Integer numMedidoresAgua,
    Integer numTimbres,
    Integer numSinServicio
) {}
