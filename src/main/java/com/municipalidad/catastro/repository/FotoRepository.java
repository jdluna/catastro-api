package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.model.Foto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class FotoRepository {

    @Inject
    DataSource dataSource;

    private static final String INSERT_SQL = """
        INSERT INTO foto (lote_id, codigo_lote, servicio, nombre, url, tipo_terreno)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, lote_id, codigo_lote, servicio, nombre, url, tipo_terreno
        FROM foto WHERE id = ?
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT id, lote_id, codigo_lote, servicio, nombre, url, tipo_terreno
        FROM foto ORDER BY id DESC LIMIT 1000
        """;

    private static final String SELECT_BY_CODIGO_LOTE_SQL = """
        SELECT id, lote_id, codigo_lote, servicio, nombre, url, tipo_terreno
        FROM foto WHERE codigo_lote = ? ORDER BY id DESC
        """;

    private static final String UPDATE_SQL = """
        UPDATE foto 
        SET lote_id = ?, codigo_lote = ?, servicio = ?, nombre = ?, url = ?, tipo_terreno = ?
        WHERE id = ?
        """;

    private static final String DELETE_SQL = "DELETE FROM foto WHERE id = ?";

    public Foto save(Foto foto) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                setFotoParams(stmt, foto);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating foto failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return findById(generatedKeys.getInt(1))
                                .orElseThrow(() -> new SQLException("Creating foto failed, no ID obtained."));
                    } else {
                        throw new SQLException("Creating foto failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<Foto> findById(Integer id) {
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

    public List<Foto> findAll() {
        return executeWithConnection(conn -> {
            List<Foto> fotos = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fotos.add(mapResultSet(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return fotos;
        });
    }

    public List<Foto> findByCodigoLote(String codigoLote) {
        return executeWithConnection(conn -> {
            List<Foto> fotos = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODIGO_LOTE_SQL)) {
                stmt.setString(1, codigoLote);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        fotos.add(mapResultSet(rs));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return fotos;
        });
    }

    public Foto update(Integer id, Foto foto) {
        return executeWithConnection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
                setFotoParams(stmt, foto);
                stmt.setInt(7, id);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating foto failed, no rows affected.");
                }

                return findById(id)
                        .orElseThrow(() -> new SQLException("Updating foto failed, record not found."));
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

    private void setFotoParams(PreparedStatement stmt, Foto foto) throws SQLException {
        stmt.setObject(1, foto.loteId());
        stmt.setString(2, foto.codigoLote());
        stmt.setString(3, foto.servicio());
        stmt.setString(4, foto.nombre());
        stmt.setString(5, foto.url());
        stmt.setString(6, foto.tipoTerreno());
    }

    private Foto mapResultSet(ResultSet rs) throws SQLException {
        return new Foto(
                rs.getInt("id"),
                (Integer) rs.getObject("lote_id"),
                rs.getString("codigo_lote"),
                rs.getString("servicio"),
                rs.getString("nombre"),
                rs.getString("url"),
                rs.getString("tipo_terreno")
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
