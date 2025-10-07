package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.Lote;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoteRepository implements PanacheRepository<Lote> {

    public Optional<Lote> findByCodigoLote(String codigoLote) {
        return find("codigoLote", codigoLote).firstResultOptional();
    }

    public List<Lote> findBySector(String codigoSector) {
        return list("codigoSector = ?1 ORDER BY codigoManzana, codigoLote", codigoSector);
    }

    public List<Lote> findByManzana(String codigoSector, String codigoManzana) {
        return list("codigoSector = ?1 and codigoManzana = ?2 ORDER BY codigoLote",
                codigoSector, codigoManzana);
    }

    public boolean existsByCodigoLote(String codigoLote) {
        return count("codigoLote", codigoLote) > 0;
    }

    public List<Lote> findAll(int page, int size) {
        return find("ORDER BY fechaCreacion DESC")
                .page(page, size)
                .list();
    }

    public long countAll() {
        return count();
    }

    public long countBySector(String codigoSector) {
        return count("codigoSector", codigoSector);
    }

    public long countByManzana(String codigoSector, String codigoManzana) {
        return count("codigoSector = ?1 and codigoManzana = ?2",
                codigoSector, codigoManzana);
    }

    public List<Lote> findWithEstimaciones() {
        return find("SELECT DISTINCT l FROM Lote l LEFT JOIN FETCH l.estimaciones")
                .list();
    }

    public List<Lote> findWithFotos() {
        return find("SELECT DISTINCT l FROM Lote l LEFT JOIN FETCH l.fotos")
                .list();
    }
}
