package com.municipalidad.catastro.model;

import java.time.LocalDate;

public record FichaCatastral(
    Integer id,
    String ubigeo,
    String sector,
    String manzana,
    String lote,
    String unidad,
    Integer piso,
    String edificio,
    String entrada,
    UbicacionLote ubicacion,
    TitularCatastral titular,
    DatosLote datosLote,
    ServiciosPrediales servicios,
    String formaTenencia,
    String origenPropiedad,
    String clasificacionPredio,
    Double areaTerreno,
    Double areaConstruida,
    String estadoConservacion,
    LocalDate fechaInscripcion,
    String numeroPartidaRegistral,
    String observaciones,
    LocalDate fechaCreacion,
    LocalDate fechaModificacion
) {
    public FichaCatastral withId(Integer newId) {
        return new FichaCatastral(newId, ubigeo, sector, manzana, lote, unidad,
                piso, edificio, entrada, ubicacion, titular, datosLote, servicios,
                formaTenencia, origenPropiedad, clasificacionPredio, areaTerreno,
                areaConstruida, estadoConservacion, fechaInscripcion,
                numeroPartidaRegistral, observaciones, fechaCreacion, fechaModificacion);
    }
}
