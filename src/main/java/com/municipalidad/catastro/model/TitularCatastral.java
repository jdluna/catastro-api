package com.municipalidad.catastro.model;

public record TitularCatastral(
    String tipoTitular,
    String tipoDocumentoIdentidad,
    String numeroDocumento,
    String apellidoPaterno,
    String apellidoMaterno,
    String nombres,
    String estadoCivil,
    String correoElectronico,
    String telefono
) {}
