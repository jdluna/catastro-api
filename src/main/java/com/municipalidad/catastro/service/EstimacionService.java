package com.municipalidad.catastro.service;

import com.municipalidad.catastro.model.Estimacion;
import com.municipalidad.catastro.repository.EstimacionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EstimacionService {

    @Inject
    EstimacionRepository repository;

    /**
     * Creates a new estimacion with timestamps
     */
    public Estimacion create(Estimacion estimacion) {
        return Optional.ofNullable(estimacion)
                .map(e -> e.withTimestamps(LocalDateTime.now()))
                .map(repository::save)
                .orElseThrow(() -> new IllegalArgumentException("Estimacion cannot be null"));
    }

    /**
     * Retrieves an estimacion by ID
     */
    public Optional<Estimacion> getById(Integer id) {
        return repository.findById(id);
    }

    /**
     * Retrieves all estimaciones
     */
    public List<Estimacion> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves estimaciones by codigo lote
     */
    public List<Estimacion> getByCodigoLote(String codigoLote) {
        if (codigoLote == null || codigoLote.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo lote cannot be null or empty");
        }
        if (!codigoLote.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Codigo lote must be exactly 8 digits");
        }
        return repository.findByCodigoLote(codigoLote);
    }

    /**
     * Updates an existing estimacion
     */
    public Estimacion update(Integer id, Estimacion estimacion) {
        return repository.findById(id)
                .map(existing -> repository.update(id, estimacion))
                .orElseThrow(() -> new IllegalArgumentException("Estimacion not found with id: " + id));
    }

    /**
     * Deletes an estimacion by ID
     */
    public boolean delete(Integer id) {
        return repository.delete(id);
    }
}
