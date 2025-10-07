package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.Estimacion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EstimacionRepository implements PanacheRepository<Estimacion> {

    public List<Estimacion> findByLoteId(Long loteId) {
        return list("lote.id ORDER BY fechaCreacion DESC", loteId);
    }

    public Optional<Estimacion> findByCodigoLote(String codigoLote) {
        return find("codigoLote ORDER BY fechaCreacion DESC", codigoLote)
                .firstResultOptional();
    }

    public List<Estimacion> findByTipoTerreno(String tipoTerreno) {
        return list("tipoTerreno", tipoTerreno);
    }

    public long countByLoteId(Long loteId) {
        return count("lote.id", loteId);
    }

    public long countByTipoTerreno(String tipoTerreno) {
        return count("tipoTerreno", tipoTerreno);
    }

    public List<Estimacion> findWithViviendas() {
        return list("numViviendas > 0 ORDER BY numViviendas DESC");
    }

    public List<Estimacion> findWithComercios() {
        return list("numComercios > 0 ORDER BY numComercios DESC");
    }

    public List<Estimacion> findByNumPisos(Integer minPisos, Integer maxPisos) {
        return list("numPisos >= ?1 and numPisos <= ?2", minPisos, maxPisos);
    }

    public Optional<Estimacion> findLatestByLoteId(Long loteId) {
        return find("lote.id ORDER BY fechaCreacion DESC", loteId)
                .firstResultOptional();
    }

    public void deleteByLoteId(Long loteId) {
        delete("lote.id", loteId);
    }
}
