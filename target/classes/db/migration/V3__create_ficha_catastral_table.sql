CREATE TABLE IF NOT EXISTS ficha_catastral (
    id SERIAL PRIMARY KEY,
    ubigeo VARCHAR(6),
    sector VARCHAR(10),
    manzana VARCHAR(10),
    lote VARCHAR(10),
    unidad VARCHAR(10),
    piso INTEGER,
    edificio VARCHAR(50),
    entrada VARCHAR(10),
    
    -- Ubicación (could be JSONB)
    latitud VARCHAR(50),
    longitud VARCHAR(50),
    
    -- Titular (could be JSONB)
    tipo_titular VARCHAR(20),
    tipo_documento VARCHAR(20),
    numero_documento VARCHAR(20),
    apellido_paterno VARCHAR(100),
    apellido_materno VARCHAR(100),
    nombres VARCHAR(100),
    estado_civil VARCHAR(20),
    correo_electronico VARCHAR(100),
    telefono VARCHAR(20),
    
    -- Datos Predio
    forma_tenencia VARCHAR(50),
    origen_propiedad VARCHAR(50),
    clasificacion_predio VARCHAR(50),
    area_terreno DECIMAL(12,2),
    area_construida DECIMAL(12,2),
    estado_conservacion VARCHAR(50),
    
    -- Registro
    fecha_inscripcion DATE,
    numero_partida_registral VARCHAR(50),
    
    observaciones TEXT,
    fecha_creacion DATE DEFAULT CURRENT_DATE,
    fecha_modificacion DATE DEFAULT CURRENT_DATE
);

CREATE INDEX idx_ficha_ubigeo ON ficha_catastral(ubigeo);
CREATE INDEX idx_ficha_sector_manzana_lote ON ficha_catastral(sector, manzana, lote);
