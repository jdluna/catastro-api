package com.municipalidad.catastro.model;

public record Foto(
    Integer id,
    Integer loteId,
    String codigoLote,
    String servicio,
    String nombre,
    String url,
    String tipoTerreno
) {
    public Foto withId(Integer newId) {
        return new Foto(newId, loteId, codigoLote, servicio, nombre, url, tipoTerreno);
    }

    public Foto withUrl(String newUrl) {
        return new Foto(id, loteId, codigoLote, servicio, nombre, newUrl, tipoTerreno);
    }
}
