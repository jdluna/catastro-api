package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.Titular;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TitularRepository implements PanacheRepository<Titular> {

    public List<Titular> findByFichaId(Long fichaId) {
        return list("ficha.id ORDER BY id", fichaId);
    }

    public Optional<Titular> findByNumeroDocumento(String numeroDocumento) {
        return find("numeroDocumento", numeroDocumento).firstResultOptional();
    }

    public List<Titular> findByTipoTitular(String tipoTitular) {
        return list("tipoTitular ORDER BY fechaCreacion DESC", tipoTitular);
    }

    public List<Titular> findByTipoDocumento(String tipoDocumento) {
        return list("tipoDocumento ORDER BY fechaCreacion DESC", tipoDocumento);
    }

    public List<Titular> findByEstadoCivil(String estadoCivil) {
        return list("estadoCivil ORDER BY fechaCreacion DESC", estadoCivil);
    }

    public List<Titular> findByCondicionTitular(String condicionTitular) {
        return list("condicionTitular ORDER BY fechaCreacion DESC", condicionTitular);
    }

    public List<Titular> findByFormaAdquisicion(String formaAdquisicion) {
        return list("formaAdquisicion ORDER BY fechaCreacion DESC", formaAdquisicion);
    }

    public List<Titular> findByApellidoPaterno(String apellidoPaterno) {
        return list("UPPER(apellidoPaterno) LIKE UPPER(?1) ORDER BY apellidoPaterno",
                "%" + apellidoPaterno + "%");
    }

    public List<Titular> findByRazonSocial(String razonSocial) {
        return list("UPPER(razonSocial) LIKE UPPER(?1) ORDER BY razonSocial",
                "%" + razonSocial + "%");
    }

    public long countByFichaId(Long fichaId) {
        return count("ficha.id", fichaId);
    }

    public void deleteByFichaId(Long fichaId) {
        delete("ficha.id", fichaId);
    }

    public List<Titular> findPersonaNatural() {
        return list("tipoTitular = '1' ORDER BY apellidoPaterno, apellidoMaterno, nombres");
    }

    public List<Titular> findPersonaJuridica() {
        return list("tipoTitular = '2' ORDER BY razonSocial");
    }
}
