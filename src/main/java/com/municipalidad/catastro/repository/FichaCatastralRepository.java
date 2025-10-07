package com.municipalidad.catastro.repository;

import com.municipalidad.catastro.domain.FichaCatastral;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FichaCatastralRepository implements PanacheRepository<FichaCatastral> {

    public List<FichaCatastral> findByCodigoLote(String codigoLote) {
        return list("codigoLote ORDER BY fechaCreacion DESC", codigoLote);
    }

    public Optional<FichaCatastral> findByCodigoCompleto(
            String codigoLote, String codigoUnidad, String codigoPiso) {
        return find("codigoLote = ?1 and codigoUnidad = ?2 and codigoPiso = ?3",
                codigoLote, codigoUnidad, codigoPiso)
                .firstResultOptional();
    }

    public List<FichaCatastral> findBySector(String codigoSector) {
        return list("codigoSector ORDER BY codigoManzana, codigoLote", codigoSector);
    }

    public List<FichaCatastral> findByManzana(String codigoSector, String codigoManzana) {
        return list("codigoSector = ?1 and codigoManzana = ?2 ORDER BY codigoLote",
                codigoSector, codigoManzana);
    }

    public List<FichaCatastral> findByFechaLevantamiento(LocalDate inicio, LocalDate fin) {
        return list("fechaLevantamiento between ?1 and ?2 ORDER BY fechaLevantamiento DESC",
                inicio, fin);
    }

    public List<FichaCatastral> findByTipoPredio(String tipoPredio) {
        return list("tipoPredio ORDER BY fechaCreacion DESC", tipoPredio);
    }

    public List<FichaCatastral> findByUsoPredio(String usoPredio) {
        return list("usoPredio ORDER BY fechaCreacion DESC", usoPredio);
    }

    public List<FichaCatastral> findByClasificacionPredio(String clasificacionPredio) {
        return list("clasificacionPredio ORDER BY fechaCreacion DESC", clasificacionPredio);
    }

    public List<FichaCatastral> findByDepartamento(String departamento) {
        return list("departamento ORDER BY provincia, distrito", departamento);
    }

    public List<FichaCatastral> findByDistrito(String distrito) {
        return list("distrito ORDER BY zonaSectorEtapa", distrito);
    }

    public List<FichaCatastral> findByAreaTerrenoRange(BigDecimal minArea, BigDecimal maxArea) {
        return list("areaTerreno >= ?1 and areaTerreno <= ?2 ORDER BY areaTerreno DESC",
                minArea, maxArea);
    }

    public List<FichaCatastral> findByAreaConstruccionRange(BigDecimal minArea, BigDecimal maxArea) {
        return list("areaConstruccion >= ?1 and areaConstruccion <= ?2 ORDER BY areaConstruccion DESC",
                minArea, maxArea);
    }

    public long countByCodigoLote(String codigoLote) {
        return count("codigoLote", codigoLote);
    }

    public long countBySector(String codigoSector) {
        return count("codigoSector", codigoSector);
    }

    public long countByTipoPredio(String tipoPredio) {
        return count("tipoPredio", tipoPredio);
    }

    public List<FichaCatastral> findAll(int page, int size) {
        return find("ORDER BY fechaCreacion DESC")
                .page(page, size)
                .list();
    }

    public List<FichaCatastral> findWithTitulares() {
        return find("SELECT DISTINCT f FROM FichaCatastral f LEFT JOIN FETCH f.titulares")
                .list();
    }

    public List<FichaCatastral> findWithConstrucciones() {
        return find("SELECT DISTINCT f FROM FichaCatastral f LEFT JOIN FETCH f.construcciones")
                .list();
    }

    public List<FichaCatastral> findWithServicios() {
        return find("SELECT DISTINCT f FROM FichaCatastral f LEFT JOIN FETCH f.servicios")
                .list();
    }

    public List<FichaCatastral> findComplete() {
        return find("SELECT DISTINCT f FROM FichaCatastral f " +
                "LEFT JOIN FETCH f.titulares " +
                "LEFT JOIN FETCH f.construcciones " +
                "LEFT JOIN FETCH f.servicios")
                .list();
    }
}
