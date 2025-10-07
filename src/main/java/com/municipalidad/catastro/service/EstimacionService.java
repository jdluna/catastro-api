package com.municipalidad.catastro.service;

import com.municipalidad.catastro.domain.Estimacion;
import com.municipalidad.catastro.dto.ApiResponse;
import com.municipalidad.catastro.dto.EstimacionDTO;
import com.municipalidad.catastro.repository.EstimacionRepository;
import com.municipalidad.catastro.repository.LoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EstimacionService {

    @Inject
    EstimacionRepository estimacionRepository;

    @Inject
    LoteRepository loteRepository;

    @Transactional
    public ApiResponse<EstimacionDTO> create(EstimacionDTO dto) {
        return loteRepository.findByCodigoLote(dto.codigoLote())
                .map(lote -> {
                    var estimacion = mapToEntity(dto);
                    estimacion.lote = lote;

                    // Calcular total de unidades catastrales si no fue proporcionado
                    if (estimacion.numUnidadesCatastrales == null || estimacion.numUnidadesCatastrales == 0) {
                        estimacion.numUnidadesCatastrales = estimacion.calcularTotalUnidades();
                    }

                    estimacionRepository.persist(estimacion);
                    lote.addEstimacion(estimacion);

                    return ApiResponse.created("Estimación creada exitosamente", mapToDTO(estimacion));
                })
                .orElse(ApiResponse.error("Lote no encontrado con código: " + dto.codigoLote()));
    }

    public ApiResponse<EstimacionDTO> findById(Long id) {
        return estimacionRepository.findByIdOptional(id)
                .map(this::mapToDTO)
                .map(ApiResponse::success)
                .orElse(ApiResponse.notFound("Estimación no encontrada con ID: " + id));
    }

    @Transactional
    public ApiResponse<EstimacionDTO> update(Long id, EstimacionDTO dto) {
        return estimacionRepository.findByIdOptional(id)
                .map(estimacion -> {
                    updateEntity(estimacion, dto);

                    // Recalcular total de unidades catastrales
                    estimacion.numUnidadesCatastrales = estimacion.calcularTotalUnidades();

                    return ApiResponse.success("Estimación actualizada exitosamente", mapToDTO(estimacion));
                })
                .orElse(ApiResponse.notFound("Estimación no encontrada con ID: " + id));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        return Optional.of(estimacionRepository.deleteById(id))
                .filter(deleted -> deleted)
                .map(deleted -> ApiResponse.<Void>success("Estimación eliminada exitosamente", null))
                .orElse(ApiResponse.notFound("Estimación no encontrada con ID: " + id));
    }

    public ApiResponse<List<EstimacionDTO>> findByLoteId(Long loteId) {
        var estimaciones = estimacionRepository.findByLoteId(loteId).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(estimaciones);
    }

    public ApiResponse<EstimacionDTO> findByCodigoLote(String codigoLote) {
        return estimacionRepository.findByCodigoLote(codigoLote)
                .map(this::mapToDTO)
                .map(ApiResponse::success)
                .orElse(ApiResponse.notFound("Estimación no encontrada para lote: " + codigoLote));
    }

    public ApiResponse<List<EstimacionDTO>> findByTipoTerreno(String tipoTerreno) {
        var estimaciones = estimacionRepository.findByTipoTerreno(tipoTerreno).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(estimaciones);
    }

    public ApiResponse<Long> countByTipoTerreno(String tipoTerreno) {
        var count = estimacionRepository.countByTipoTerreno(tipoTerreno);
        return ApiResponse.success(count);
    }

    private Estimacion mapToEntity(EstimacionDTO dto) {
        var e = new Estimacion();
        e.codigoLote = dto.codigoLote();
        e.numUnidadesCatastrales = dto.numUnidadesCatastrales() != null ? dto.numUnidadesCatastrales() : 0;
        e.tipoTerreno = dto.tipoTerreno();
        e.numPisos = dto.numPisos() != null ? dto.numPisos() : 0;
        e.numViviendas = dto.numViviendas() != null ? dto.numViviendas() : 0;
        e.numComercios = dto.numComercios() != null ? dto.numComercios() : 0;
        e.numIndustrias = dto.numIndustrias() != null ? dto.numIndustrias() : 0;
        e.numEducacion = dto.numEducacion() != null ? dto.numEducacion() : 0;
        e.numSalud = dto.numSalud() != null ? dto.numSalud() : 0;
        e.numReligion = dto.numReligion() != null ? dto.numReligion() : 0;
        e.numEstacionamientos = dto.numEstacionamientos() != null ? dto.numEstacionamientos() : 0;
        e.numMedidoresLuz = dto.numMedidoresLuz() != null ? dto.numMedidoresLuz() : 0;
        e.numMedidoresAgua = dto.numMedidoresAgua() != null ? dto.numMedidoresAgua() : 0;
        e.numTimbres = dto.numTimbres() != null ? dto.numTimbres() : 0;
        e.numSinServicio = dto.numSinServicio() != null ? dto.numSinServicio() : 0;
        e.observacion = dto.observacion();
        return e;
    }

    private EstimacionDTO mapToDTO(Estimacion e) {
        return new EstimacionDTO(
                e.id,
                e.lote != null ? e.lote.id : null,
                e.codigoLote,
                e.numUnidadesCatastrales,
                e.tipoTerreno,
                e.numPisos,
                e.numViviendas,
                e.numComercios,
                e.numIndustrias,
                e.numEducacion,
                e.numSalud,
                e.numReligion,
                e.numEstacionamientos,
                e.numMedidoresLuz,
                e.numMedidoresAgua,
                e.numTimbres,
                e.numSinServicio,
                e.observacion
        );
    }

    private void updateEntity(Estimacion e, EstimacionDTO dto) {
        e.numUnidadesCatastrales = dto.numUnidadesCatastrales() != null ? dto.numUnidadesCatastrales() : 0;
        e.tipoTerreno = dto.tipoTerreno();
        e.numPisos = dto.numPisos() != null ? dto.numPisos() : 0;
        e.numViviendas = dto.numViviendas() != null ? dto.numViviendas() : 0;
        e.numComercios = dto.numComercios() != null ? dto.numComercios() : 0;
        e.numIndustrias = dto.numIndustrias() != null ? dto.numIndustrias() : 0;
        e.numEducacion = dto.numEducacion() != null ? dto.numEducacion() : 0;
        e.numSalud = dto.numSalud() != null ? dto.numSalud() : 0;
        e.numReligion = dto.numReligion() != null ? dto.numReligion() : 0;
        e.numEstacionamientos = dto.numEstacionamientos() != null ? dto.numEstacionamientos() : 0;
        e.numMedidoresLuz = dto.numMedidoresLuz() != null ? dto.numMedidoresLuz() : 0;
        e.numMedidoresAgua = dto.numMedidoresAgua() != null ? dto.numMedidoresAgua() : 0;
        e.numTimbres = dto.numTimbres() != null ? dto.numTimbres() : 0;
        e.numSinServicio = dto.numSinServicio() != null ? dto.numSinServicio() : 0;
        e.observacion = dto.observacion();
    }
}
