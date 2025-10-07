package com.municipalidad.catastro.service;

import com.municipalidad.catastro.domain.Lote;
import com.municipalidad.catastro.dto.ApiResponse;
import com.municipalidad.catastro.dto.LoteDTO;
import com.municipalidad.catastro.repository.LoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ApplicationScoped
public class LoteService {

    @Inject
    LoteRepository loteRepository;

    @Transactional
    public ApiResponse<LoteDTO> create(LoteDTO dto) {
        if (loteRepository.existsByCodigoLote(dto.codigoLote())) {
            return ApiResponse.error("Ya existe un lote con código: " + dto.codigoLote());
        }

        var lote = mapToEntity(dto);
        loteRepository.persist(lote);
        return ApiResponse.created("Lote creado exitosamente", mapToDTO(lote));
    }

    public ApiResponse<LoteDTO> findById(Long id) {
        return loteRepository.findByIdOptional(id)
                .map(this::mapToDTO)
                .map(ApiResponse::success)
                .orElse(ApiResponse.notFound("Lote no encontrado con ID: " + id));
    }

    public ApiResponse<LoteDTO> findByCodigoLote(String codigoLote) {
        return loteRepository.findByCodigoLote(codigoLote)
                .map(this::mapToDTO)
                .map(ApiResponse::success)
                .orElse(ApiResponse.notFound("Lote no encontrado con código: " + codigoLote));
    }

    @Transactional
    public ApiResponse<LoteDTO> update(Long id, LoteDTO dto) {
        return loteRepository.findByIdOptional(id)
                .map(lote -> {
                    updateEntity(lote, dto);
                    return ApiResponse.success("Lote actualizado exitosamente", mapToDTO(lote));
                })
                .orElse(ApiResponse.notFound("Lote no encontrado con ID: " + id));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        return Optional.of(loteRepository.deleteById(id))
                .filter(deleted -> deleted)
                .map(deleted -> ApiResponse.<Void>success("Lote eliminado exitosamente", null))
                .orElse(ApiResponse.notFound("Lote no encontrado con ID: " + id));
    }

    public ApiResponse<List<LoteDTO>> findBySector(String codigoSector) {
        var lotes = loteRepository.findBySector(codigoSector).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(lotes);
    }

    public ApiResponse<List<LoteDTO>> findByManzana(String codigoSector, String codigoManzana) {
        var lotes = loteRepository.findByManzana(codigoSector, codigoManzana).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(lotes);
    }

    public ApiResponse<List<LoteDTO>> findAll(int page, int size) {
        var lotes = loteRepository.findAll(page, size).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(lotes);
    }

    public ApiResponse<Long> countBySector(String codigoSector) {
        var count = loteRepository.countBySector(codigoSector);
        return ApiResponse.success(count);
    }

    public ApiResponse<Long> countByManzana(String codigoSector, String codigoManzana) {
        var count = loteRepository.countByManzana(codigoSector, codigoManzana);
        return ApiResponse.success(count);
    }

    private Lote mapToEntity(LoteDTO dto) {
        var lote = new Lote();
        lote.codigoSector = dto.codigoSector();
        lote.codigoManzana = dto.codigoManzana();
        lote.codigoLote = dto.codigoLote();
        lote.latitud = dto.latitud();
        lote.longitud = dto.longitud();
        lote.precisionMetros = dto.precisionMetros();
        return lote;
    }

    private LoteDTO mapToDTO(Lote entity) {
        return new LoteDTO(
                entity.id,
                entity.codigoSector,
                entity.codigoManzana,
                entity.codigoLote,
                entity.latitud,
                entity.longitud,
                entity.precisionMetros,
                entity.fechaCreacion,
                entity.fechaModificacion
        );
    }

    private void updateEntity(Lote entity, LoteDTO dto) {
        entity.codigoSector = dto.codigoSector();
        entity.codigoManzana = dto.codigoManzana();
        entity.latitud = dto.latitud();
        entity.longitud = dto.longitud();
        entity.precisionMetros = dto.precisionMetros();
    }
}
