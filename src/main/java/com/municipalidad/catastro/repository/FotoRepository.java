package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.Foto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FotoRepository implements PanacheRepository<Foto> {

    public List<Foto> findByLoteId(Long loteId) {
        return list("lote.id ORDER BY fechaCreacion DESC", loteId);
    }

    public List<Foto> findByCodigoLote(String codigoLote) {
        return list("codigoLote ORDER BY fechaCreacion DESC", codigoLote);
    }

    public Optional<Foto> findByUrl(String url) {
        return find("url", url).firstResultOptional();
    }

    public List<Foto> findByServicio(String servicio) {
        return list("servicio ORDER BY fechaCreacion DESC", servicio);
    }

    public List<Foto> findByTipoFoto(String tipoFoto) {
        return list("tipoFoto ORDER BY fechaCreacion DESC", tipoFoto);
    }

    public List<Foto> findByContentType(String contentType) {
        return list("contentType ORDER BY fechaCreacion DESC", contentType);
    }

    public long countByLoteId(Long loteId) {
        return count("lote.id", loteId);
    }

    public long countByCodigoLote(String codigoLote) {
        return count("codigoLote", codigoLote);
    }

    public void deleteByLoteId(Long loteId) {
        delete("lote.id", loteId);
    }

    public void deleteByCodigoLote(String codigoLote) {
        delete("codigoLote", codigoLote);
    }

    public List<Foto> findByTipoTerrenoAndLote(String tipoTerreno, Long loteId) {
        return list("tipoTerreno = ?1 and lote.id = ?2 ORDER BY fechaCreacion DESC",
                tipoTerreno, loteId);
    }
}
