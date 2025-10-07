package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.model.Estimacion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class EstimacionRepository {

    @Inject
    DataSource dataSource;

    private static final String INSERT_SQL = """
        INSERT INTO estimacion (lote_id, codigo_lote, num_unidades_catastrales, tipo_terreno,
                                num_viviendas, num_comercios, num_industrias, num_educacion,
                                num_salud, num_religion, num_estacionamientos, observacion,
                                fecha_creacion, fecha_modificacion)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, lote_id, codigo_lote, num_unidades_catastrales, tipo_terreno,
               num_viviendas, num_comercios, num_industrias, num_educacion,
               num_salud, num_religion, num_estacionamientos, observacion,
               fecha_creacion, fecha_modificacion
        FROM estimacion WHERE id = ?
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT id, lote_id, codigo_lote, num_unidades_catastrales, tipo_terreno,
               num_viviendas, num_comercios, num_industrias, num_educacion,
               num_salud, num_religion, num_estacionamientos, observacion,
               fecha_creacion, fecha_modificacion
        FROM estimacion ORDER BY fecha_creacion DESC LIMIT 1000
        """;

    private static final String SELECT_BY_CODIGO_LOTE_SQL = """
        SELECT id, lote_id, codigo_lote, num_unidades_catastrales, tipo_terreno,
               num_viviendas, num_comercios, num_industrias, num_educacion,
               num_salud, num_religion, num_estacionamientos, observacion,
               fecha_creacion, fecha_modificacion
        FROM estimacion WHERE codigo_lote = ? ORDER BY fecha_creacion DESC
        """;

    private static final String UPDATE_SQL = """
        UPDATE estimacion 
        SET lote_id = ?, codigo_lote = ?, num_unidades_catastrales = ?,
            tipo_terreno = ?, num_viviendas = ?, num_comercios = ?,
            num_industrias = ?, num_educacion = ?, num_salud = ?,
            num_religion = ?, num_estacionamientos = ?, observacion = ?,
            fecha_modificacion = CURRENT_TIMESTAMP
        WHERE id = ?
        """;

    private static final String DELETE_SQL = "DELETE FROM estimacion WHERE id = ?";

    public Estimacion save(Estimacion estimacion) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                setEstimacionParams(stmt, estimacion);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating estimacion failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return findById(generatedKeys.getInt(1))
                                .orElseThrow(() -> new SQLException("Creating estimacion failed, no ID obtained."));
                    } else {
                        throw new SQLException("Creating estimacion failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<Estimacion> findById(Integer id) {
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

    public List<Estimacion> findAll() {
        return executeWithConnection(conn -> {
            List<Estimacion> estimaciones = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    estimaciones.add(mapResultSet(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return estimaciones;
        });
    }

    public List<Estimacion> findByCodigoLote(String codigoLote) {
        return executeWithConnection(conn -> {
            List<Estimacion> estimaciones = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODIGO_LOTE_SQL)) {
                stmt.setString(1, codigoLote);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        estimaciones.add(mapResultSet(rs));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return estimaciones;
        });
    }

    public Estimacion update(Integer id, Estimacion estimacion) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
                setEstimacionParams(stmt, estimacion);
                stmt.setInt(13, id);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating estimacion failed, no rows affected.");
                }

                return findById(id)
                        .orElseThrow(() -> new SQLException("Updating estimacion failed, record not found."));
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

    private void setEstimacionParams(PreparedStatement stmt, Estimacion est) throws SQLException {
        stmt.setObject(1, est.loteId());
        stmt.setString(2, est.codigoLote());
        stmt.setInt(3, est.numUnidadesCatastrales() != null ? est.numUnidadesCatastrales() : 0);
        stmt.setString(4, est.tipoTerreno());
        stmt.setInt(5, est.numViviendas() != null ? est.numViviendas() : 0);
        stmt.setInt(6, est.numComercios() != null ? est.numComercios() : 0);
        stmt.setInt(7, est.numIndustrias() != null ? est.numIndustrias() : 0);
        stmt.setInt(8, est.numEducacion() != null ? est.numEducacion() : 0);
        stmt.setInt(9, est.numSalud() != null ? est.numSalud() : 0);
        stmt.setInt(10, est.numReligion() != null ? est.numReligion() : 0);
        stmt.setInt(11, est.numEstacionamientos() != null ? est.numEstacionamientos() : 0);
        stmt.setString(12, est.observacion());
    }

    private Estimacion mapResultSet(ResultSet rs) throws SQLException {
        return new Estimacion(
                rs.getInt("id"),
                (Integer) rs.getObject("lote_id"),
                rs.getString("codigo_lote"),
                rs.getInt("num_unidades_catastrales"),
                rs.getString("tipo_terreno"),
                rs.getInt("num_viviendas"),
                rs.getInt("num_comercios"),
                rs.getInt("num_industrias"),
                rs.getInt("num_educacion"),
                rs.getInt("num_salud"),
                rs.getInt("num_religion"),
                rs.getInt("num_estacionamientos"),
                rs.getString("observacion"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getTimestamp("fecha_modificacion").toLocalDateTime()
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
