package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.Servicio;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ServicioRepository implements PanacheRepository<Servicio> {

    public Optional<Servicio> findByFichaId(Long fichaId) {
        return find("ficha.id", fichaId).firstResultOptional();
    }

    public void deleteByFichaId(Long fichaId) {
        delete("ficha.id", fichaId);
    }

    public List<Servicio> findWithLuz() {
        return list("tieneLuz = true ORDER BY fechaCreacion DESC");
    }

    public List<Servicio> findWithAgua() {
        return list("tieneAgua = true ORDER BY fechaCreacion DESC");
    }

    public List<Servicio> findWithDesague() {
        return list("tieneDesague = true ORDER BY fechaCreacion DESC");
    }

    public List<Servicio> findWithInternet() {
        return list("tieneInternet = true ORDER BY fechaCreacion DESC");
    }

    public List<Servicio> findByTipoLuz(String tipoLuz) {
        return list("tipoLuz ORDER BY fechaCreacion DESC", tipoLuz);
    }

    public List<Servicio> findByTipoAgua(String tipoAgua) {
        return list("tipoAgua ORDER BY fechaCreacion DESC", tipoAgua);
    }

    public long countWithAllBasicServices() {
        return count("tieneLuz = true and tieneAgua = true and tieneDesague = true");
    }

    public long countWithoutBasicServices() {
        return count("(tieneLuz = false or tieneLuz is null) and " +
                "(tieneAgua = false or tieneAgua is null) and " +
                "(tieneDesague = false or tieneDesague is null)");
    }
}
