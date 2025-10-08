-- Tabla Estimación (Pantallas 1-4)
CREATE TABLE IF NOT EXISTS estimacion (
    id SERIAL PRIMARY KEY,
    num_viviendas INTEGER DEFAULT 0,
    num_comercios INTEGER DEFAULT 0,
    num_industrias INTEGER DEFAULT 0,
    num_educacion INTEGER DEFAULT 0,
    num_salud INTEGER DEFAULT 0,
    num_religion INTEGER DEFAULT 0,
    num_estacionamientos INTEGER DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Ficha Catastral (Etapa 2)
CREATE TABLE IF NOT EXISTS ficha_catastral (
    id SERIAL PRIMARY KEY,
    codigo_lote CHAR(8) NOT NULL,
    codigo_sector VARCHAR(2),
    codigo_manzana VARCHAR(3),
    codigo_unidad VARCHAR(3),
    codigo_piso VARCHAR(2),
    codigo_edificacion VARCHAR(2),
    codigo_entrada VARCHAR(2),
    contador_fichas INTEGER,
    tipo_predio VARCHAR(50),
    clasificacion_predio VARCHAR(50),
    uso_predio VARCHAR(50),
    frente_ml DECIMAL(10, 2),
    derecha_ml DECIMAL(10, 2),
    izquierda_ml DECIMAL(10, 2),
    fondo_ml DECIMAL(10, 2),
    area_terreno DECIMAL(10, 2),
    area_construccion DECIMAL(10, 2),
    area_verificada DECIMAL(10, 2),
    fecha_levantamiento DATE,
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Titular
CREATE TABLE IF NOT EXISTS titular (
    id SERIAL PRIMARY KEY,
    ficha_id INTEGER REFERENCES ficha_catastral(id) ON DELETE CASCADE,
    tipo_titular VARCHAR(20),
    tipo_documento VARCHAR(20),
    numero_documento VARCHAR(20),
    apellido_paterno VARCHAR(100),
    apellido_materno VARCHAR(100),
    nombres VARCHAR(100),
    razon_social VARCHAR(200),
    estado_civil VARCHAR(20),
    porcentaje_propiedad DECIMAL(5, 2),
    forma_adquisicion VARCHAR(50),
    fecha_adquisicion DATE,
    tipo_documento_legal VARCHAR(50),
    numero_partida VARCHAR(50),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Construcción
CREATE TABLE IF NOT EXISTS construccion (
    id SERIAL PRIMARY KEY,
    ficha_id INTEGER REFERENCES ficha_catastral(id) ON DELETE CASCADE,
    numero_piso INTEGER,
    fecha_construccion DATE,
    material_estructural VARCHAR(50),
    estado_conservacion VARCHAR(20),
    estado_construccion VARCHAR(20),
    area_construida DECIMAL(10, 2),
    muros VARCHAR(10),
    techos VARCHAR(10),
    pisos VARCHAR(10),
    puertas_ventanas VARCHAR(10),
    revestimiento VARCHAR(10),
    instalaciones_sanitarias VARCHAR(10),
    instalaciones_electricas VARCHAR(10),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Servicios
CREATE TABLE IF NOT EXISTS servicio (
    id SERIAL PRIMARY KEY,
    ficha_id INTEGER REFERENCES ficha_catastral(id) ON DELETE CASCADE,
    tiene_luz BOOLEAN DEFAULT false,
    tiene_agua BOOLEAN DEFAULT false,
    tiene_desague BOOLEAN DEFAULT false,
    tiene_gas BOOLEAN DEFAULT false,
    tiene_internet BOOLEAN DEFAULT false,
    tiene_tv_cable BOOLEAN DEFAULT false,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_titular_ficha ON titular(ficha_id);
CREATE INDEX idx_construccion_ficha ON construccion(ficha_id);
CREATE INDEX idx_servicio_ficha ON servicio(ficha_id);
