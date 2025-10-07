package com.municipalidad.catastro.service;

import com.municipalidad.catastro.model.FichaCatastral;
import com.municipalidad.catastro.repository.FichaCatastralRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class FichaCatastralService {

    @Inject
    FichaCatastralRepository repository;

    /**
     * Creates a new ficha catastral with timestamps
     */
    public FichaCatastral create(FichaCatastral ficha) {
        return Optional.ofNullable(ficha)
                .map(this::withCreationTimestamps)
                .map(repository::save)
                .orElseThrow(() -> new IllegalArgumentException("Ficha catastral cannot be null"));
    }

    /**
     * Retrieves a ficha catastral by ID
     */
    public Optional<FichaCatastral> getById(Integer id) {
        return repository.findById(id);
    }

    /**
     * Retrieves all fichas catastrales
     */
    public List<FichaCatastral> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves fichas catastrales by sector, manzana and lote
     */
    public List<FichaCatastral> getBySectorManzanaLote(String sector, String manzana, String lote) {
        validateParameters(sector, manzana, lote);
        return repository.findBySectorManzanaLote(sector, manzana, lote);
    }

    /**
     * Retrieves fichas catastrales by ubigeo
     */
    public List<FichaCatastral> getByUbigeo(String ubigeo) {
        if (ubigeo == null || ubigeo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ubigeo cannot be null or empty");
        }
        return repository.findByUbigeo(ubigeo);
    }

    /**
     * Updates an existing ficha catastral
     */
    public FichaCatastral update(Integer id, FichaCatastral ficha) {
        return repository.findById(id)
                .map(existing -> withUpdateTimestamp(ficha, existing))
                .map(updated -> repository.update(id, updated))
                .orElseThrow(() -> new IllegalArgumentException("Ficha catastral not found with id: " + id));
    }

    /**
     * Deletes a ficha catastral by ID
     */
    public boolean delete(Integer id) {
        return repository.delete(id);
    }

    // Private helper methods using functional approach

    private FichaCatastral withCreationTimestamps(FichaCatastral ficha) {
        LocalDate now = LocalDate.now();
        return new FichaCatastral(
                null,
                ficha.ubigeo(),
                ficha.sector(),
                ficha.manzana(),
                ficha.lote(),
                ficha.unidad(),
                ficha.piso(),
                ficha.edificio(),
                ficha.entrada(),
                ficha.ubicacion(),
                ficha.titular(),
                ficha.datosLote(),
                ficha.servicios(),
                ficha.formaTenencia(),
                ficha.origenPropiedad(),
                ficha.clasificacionPredio(),
                ficha.areaTerreno(),
                ficha.areaConstruida(),
                ficha.estadoConservacion(),
                ficha.fechaInscripcion(),
                ficha.numeroPartidaRegistral(),
                ficha.observaciones(),
                now,
                now
        );
    }

    private FichaCatastral withUpdateTimestamp(FichaCatastral ficha, FichaCatastral existing) {
        return new FichaCatastral(
                existing.id(),
                ficha.ubigeo(),
                ficha.sector(),
                ficha.manzana(),
                ficha.lote(),
                ficha.unidad(),
                ficha.piso(),
                ficha.edificio(),
                ficha.entrada(),
                ficha.ubicacion(),
                ficha.titular(),
                ficha.datosLote(),
                ficha.servicios(),
                ficha.formaTenencia(),
                ficha.origenPropiedad(),
                ficha.clasificacionPredio(),
                ficha.areaTerreno(),
                ficha.areaConstruida(),
                ficha.estadoConservacion(),
                ficha.fechaInscripcion(),
                ficha.numeroPartidaRegistral(),
                ficha.observaciones(),
                existing.fechaCreacion(),
                LocalDate.now()
        );
    }

    private void validateParameters(String sector, String manzana, String lote) {
        if (sector == null || sector.trim().isEmpty()) {
            throw new IllegalArgumentException("Sector cannot be null or empty");
        }
        if (manzana == null || manzana.trim().isEmpty()) {
            throw new IllegalArgumentException("Manzana cannot be null or empty");
        }
        if (lote == null || lote.trim().isEmpty()) {
            throw new IllegalArgumentException("Lote cannot be null or empty");
        }
    }
}
