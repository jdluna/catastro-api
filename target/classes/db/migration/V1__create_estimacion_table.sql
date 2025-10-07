CREATE TABLE IF NOT EXISTS estimacion (
    id SERIAL PRIMARY KEY,
    lote_id INTEGER,
    codigo_lote CHAR(8) CHECK (codigo_lote ~ '^\d{8}$'),
    num_unidades_catastrales INTEGER DEFAULT 0,
    tipo_terreno VARCHAR(20),
    num_viviendas INTEGER DEFAULT 0,
    num_comercios INTEGER DEFAULT 0,
    num_industrias INTEGER DEFAULT 0,
    num_educacion INTEGER DEFAULT 0,
    num_salud INTEGER DEFAULT 0,
    num_religion INTEGER DEFAULT 0,
    num_estacionamientos INTEGER DEFAULT 0,
    observacion VARCHAR(250),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_estimacion_codigo_lote ON estimacion(codigo_lote);
CREATE INDEX idx_estimacion_lote_id ON estimacion(lote_id);
