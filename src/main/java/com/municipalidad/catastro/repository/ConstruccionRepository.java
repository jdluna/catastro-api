package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.Construccion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ConstruccionRepository implements PanacheRepository<Construccion> {

    public List<Construccion> findByFichaId(Long fichaId) {
        return list("ficha.id ORDER BY numeroPiso", fichaId);
    }

    public List<Construccion> findByEstadoConservacion(String estado) {
        return list("estadoConservacion ORDER BY fechaCreacion DESC", estado);
    }

    public List<Construccion> findByEstadoConstruccion(String estado) {
        return list("estadoConstruccion ORDER BY fechaCreacion DESC", estado);
    }

    public List<Construccion> findByMaterialEstructural(String material) {
        return list("materialEstructural ORDER BY fechaCreacion DESC", material);
    }

    public List<Construccion> findByNumeroPiso(Integer numeroPiso) {
        return list("numeroPiso ORDER BY fechaCreacion DESC", numeroPiso);
    }

    public List<Construccion> findByAreaConstruidaRange(BigDecimal minArea, BigDecimal maxArea) {
        return list("areaConstruida >= ?1 and areaConstruida <= ?2 ORDER BY areaConstruida DESC",
                minArea, maxArea);
    }

    public long countByFichaId(Long fichaId) {
        return count("ficha.id", fichaId);
    }

    public long countByEstadoConservacion(String estado) {
        return count("estadoConservacion", estado);
    }

    public void deleteByFichaId(Long fichaId) {
        delete("ficha.id", fichaId);
    }

    public List<Construccion> findWithGarage() {
        return list("tieneGarage = true ORDER BY fechaCreacion DESC");
    }

    public BigDecimal sumAreaConstruidaByFichaId(Long fichaId) {
        return find("SELECT COALESCE(SUM(c.areaConstruida), 0) FROM Construccion c WHERE c.ficha.id = ?1", fichaId)
                .project(BigDecimal.class)
                .firstResult();
    }
}
