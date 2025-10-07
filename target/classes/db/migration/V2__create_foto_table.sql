CREATE TABLE IF NOT EXISTS foto (
    id SERIAL PRIMARY KEY,
    lote_id INTEGER,
    codigo_lote CHAR(8) CHECK (codigo_lote ~ '^\d{8}$'),
    servicio VARCHAR(20),
    nombre VARCHAR(100),
    url VARCHAR(255),
    tipo_terreno VARCHAR(20)
);

CREATE INDEX idx_foto_codigo_lote ON foto(codigo_lote);
CREATE INDEX idx_foto_lote_id ON foto(lote_id);
