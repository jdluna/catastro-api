package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class FichaCatastralRepository {

    @Inject
    DataSource dataSource;

    private static final String INSERT_SQL = """
        INSERT INTO ficha_catastral (
            ubigeo, sector, manzana, lote, unidad, piso, edificio, entrada,
            latitud, longitud,
            tipo_titular, tipo_documento, numero_documento, apellido_paterno, apellido_materno,
            nombres, estado_civil, correo_electronico, telefono,
            forma_tenencia, origen_propiedad, clasificacion_predio,
            area_terreno, area_construida, estado_conservacion,
            fecha_inscripcion, numero_partida_registral, observaciones,
            fecha_creacion, fecha_modificacion
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT * FROM ficha_catastral WHERE id = ?
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT * FROM ficha_catastral ORDER BY fecha_creacion DESC LIMIT 100
        """;

    private static final String SELECT_BY_UBIGEO_SQL = """
        SELECT * FROM ficha_catastral WHERE ubigeo = ? ORDER BY fecha_creacion DESC LIMIT 100
        """;

    private static final String SELECT_BY_SECTOR_MANZANA_LOTE_SQL = """
        SELECT * FROM ficha_catastral 
        WHERE sector = ? AND manzana = ? AND lote = ?
        ORDER BY fecha_creacion DESC
        """;

    private static final String UPDATE_SQL = """
        UPDATE ficha_catastral SET
            ubigeo = ?, sector = ?, manzana = ?, lote = ?, unidad = ?, piso = ?,
            edificio = ?, entrada = ?, latitud = ?, longitud = ?,
            tipo_titular = ?, tipo_documento = ?, numero_documento = ?,
            apellido_paterno = ?, apellido_materno = ?, nombres = ?,
            estado_civil = ?, correo_electronico = ?, telefono = ?,
            forma_tenencia = ?, origen_propiedad = ?, clasificacion_predio = ?,
            area_terreno = ?, area_construida = ?, estado_conservacion = ?,
            fecha_inscripcion = ?, numero_partida_registral = ?, observaciones = ?,
            fecha_modificacion = CURRENT_DATE
        WHERE id = ?
        """;

    private static final String DELETE_SQL = "DELETE FROM ficha_catastral WHERE id = ?";

    public FichaCatastral save(FichaCatastral ficha) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                setFichaCatastralParams(stmt, ficha);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating ficha catastral failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return findById(generatedKeys.getInt(1))
                                .orElseThrow(() -> new SQLException("Creating ficha catastral failed, no ID obtained."));
                    } else {
                        throw new SQLException("Creating ficha catastral failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<FichaCatastral> findById(Integer id) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? Optional.of(mapResultSet(rs)) : Optional.empty();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<FichaCatastral> findAll() {
        return executeWithConnection(conn -> {
            List<FichaCatastral> fichas = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fichas.add(mapResultSet(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return fichas;
        });
    }

    public List<FichaCatastral> findByUbigeo(String ubigeo) {
        return executeWithConnection(conn -> {
            List<FichaCatastral> fichas = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_UBIGEO_SQL)) {
                stmt.setString(1, ubigeo);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        fichas.add(mapResultSet(rs));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return fichas;
        });
    }

    public List<FichaCatastral> findBySectorManzanaLote(String sector, String manzana, String lote) {
        return executeWithConnection(conn -> {
            List<FichaCatastral> fichas = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_SECTOR_MANZANA_LOTE_SQL)) {
                stmt.setString(1, sector);
                stmt.setString(2, manzana);
                stmt.setString(3, lote);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        fichas.add(mapResultSet(rs));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return fichas;
        });
    }

    public FichaCatastral update(Integer id, FichaCatastral ficha) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
                setFichaCatastralParams(stmt, ficha);
                stmt.setInt(29, id);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating ficha catastral failed, no rows affected.");
                }

                return findById(id)
                        .orElseThrow(() -> new SQLException("Updating ficha catastral failed, record not found."));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean delete(Integer id) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setFichaCatastralParams(PreparedStatement stmt, FichaCatastral ficha) throws SQLException {
        // Basic fields
        stmt.setString(1, ficha.ubigeo());
        stmt.setString(2, ficha.sector());
        stmt.setString(3, ficha.manzana());
        stmt.setString(4, ficha.lote());
        stmt.setString(5, ficha.unidad());
        stmt.setObject(6, ficha.piso());
        stmt.setString(7, ficha.edificio());
        stmt.setString(8, ficha.entrada());

        // Ubicacion
        if (ficha.ubicacion() != null) {
            stmt.setString(9, ficha.ubicacion().latitud());
            stmt.setString(10, ficha.ubicacion().longitud());
        } else {
            stmt.setNull(9, Types.VARCHAR);
            stmt.setNull(10, Types.VARCHAR);
        }

        // Titular
        if (ficha.titular() != null) {
            stmt.setString(11, ficha.titular().tipoTitular());
            stmt.setString(12, ficha.titular().tipoDocumentoIdentidad());
            stmt.setString(13, ficha.titular().numeroDocumento());
            stmt.setString(14, ficha.titular().apellidoPaterno());
            stmt.setString(15, ficha.titular().apellidoMaterno());
            stmt.setString(16, ficha.titular().nombres());
            stmt.setString(17, ficha.titular().estadoCivil());
            stmt.setString(18, ficha.titular().correoElectronico());
            stmt.setString(19, ficha.titular().telefono());
        } else {
            for (int i = 11; i <= 19; i++) {
                stmt.setNull(i, Types.VARCHAR);
            }
        }

        // Property details
        stmt.setString(20, ficha.formaTenencia());
        stmt.setString(21, ficha.origenPropiedad());
        stmt.setString(22, ficha.clasificacionPredio());
        stmt.setObject(23, ficha.areaTerreno());
        stmt.setObject(24, ficha.areaConstruida());
        stmt.setString(25, ficha.estadoConservacion());
        stmt.setDate(26, ficha.fechaInscripcion() != null ? Date.valueOf(ficha.fechaInscripcion()) : null);
        stmt.setString(27, ficha.numeroPartidaRegistral());
        stmt.setString(28, ficha.observaciones());
    }

    private FichaCatastral mapResultSet(ResultSet rs) throws SQLException {
        // Map Ubicacion
        UbicacionLote ubicacion = new UbicacionLote(
                rs.getString("latitud"),
                rs.getString("longitud"),
                rs.getString("sector"),
                rs.getString("manzana"),
                null, null, null // departamento, provincia, distrito not in table
        );

        // Map Titular
        TitularCatastral titular = new TitularCatastral(
                rs.getString("tipo_titular"),
                rs.getString("tipo_documento"),
                rs.getString("numero_documento"),
                rs.getString("apellido_paterno"),
                rs.getString("apellido_materno"),
                rs.getString("nombres"),
                rs.getString("estado_civil"),
                rs.getString("correo_electronico"),
                rs.getString("telefono")
        );

        return new FichaCatastral(
                rs.getInt("id"),
                rs.getString("ubigeo"),
                rs.getString("sector"),
                rs.getString("manzana"),
                rs.getString("lote"),
                rs.getString("unidad"),
                (Integer) rs.getObject("piso"),
                rs.getString("edificio"),
                rs.getString("entrada"),
                ubicacion,
                titular,
                null, // datosLote - not in simplified table
                null, // servicios - not in simplified table
                rs.getString("forma_tenencia"),
                rs.getString("origen_propiedad"),
                rs.getString("clasificacion_predio"),
                (Double) rs.getObject("area_terreno"),
                (Double) rs.getObject("area_construida"),
                rs.getString("estado_conservacion"),
                rs.getDate("fecha_inscripcion") != null ? rs.getDate("fecha_inscripcion").toLocalDate() : null,
                rs.getString("numero_partida_registral"),
                rs.getString("observaciones"),
                rs.getDate("fecha_creacion").toLocalDate(),
                rs.getDate("fecha_modificacion").toLocalDate()
        );
    }

    private <T> T executeWithConnection(Function<Connection, T> function) {
        try (Connection conn = dataSource.getConnection()) {
            return function.apply(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}
