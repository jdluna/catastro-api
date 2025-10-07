package com.municipalidad.catastro.service;

import com.municipalidad.catastro.domain.Foto;
import com.municipalidad.catastro.dto.ApiResponse;
import com.municipalidad.catastro.dto.FotoDTO;
import com.municipalidad.catastro.repository.FotoRepository;
import com.municipalidad.catastro.repository.LoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FotoService {

    @Inject
    FotoRepository fotoRepository;

    @Inject
    LoteRepository loteRepository;

    @Transactional
    public ApiResponse<FotoDTO> create(FotoDTO dto) {
        return loteRepository.findByCodigoLote(dto.codigoLote())
                .map(lote -> {
                    var foto = mapToEntity(dto);
                    foto.lote = lote;
                    fotoRepository.persist(foto);
                    lote.addFoto(foto);
                    return ApiResponse.created("Foto registrada exitosamente", mapToDTO(foto));
                })
                .orElse(ApiResponse.error("Lote no encontrado con código: " + dto.codigoLote()));
    }

    public ApiResponse<FotoDTO> findById(Long id) {
        return fotoRepository.findByIdOptional(id)
                .map(this::mapToDTO)
                .map(ApiResponse::success)
                .orElse(ApiResponse.notFound("Foto no encontrada con ID: " + id));
    }

    @Transactional
    public ApiResponse<Void> delete(Long id) {
        return fotoRepository.findByIdOptional(id)
                .map(foto -> {
                    //delete de S3
                    fotoRepository.delete(foto);
                    return ApiResponse.<Void>success("Foto eliminada exitosamente", null);
                })
                .orElse(ApiResponse.notFound("Foto no encontrada con ID: " + id));
    }

    public ApiResponse<List<FotoDTO>> findByLoteId(Long loteId) {
        var fotos = fotoRepository.findByLoteId(loteId).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fotos);
    }

    public ApiResponse<List<FotoDTO>> findByCodigoLote(String codigoLote) {
        var fotos = fotoRepository.findByCodigoLote(codigoLote).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fotos);
    }

    public ApiResponse<List<FotoDTO>> findByTipoFoto(String tipoFoto) {
        var fotos = fotoRepository.findByTipoFoto(tipoFoto).stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(fotos);
    }

    /*
    @Transactional
    public ApiResponse<FotoDTO> uploadToS3(String codigoLote, byte[] fileData,
                                           String fileName, String contentType) {
        return loteRepository.findByCodigoLote(codigoLote)
                .map(lote -> {
                    var s3Key = "lote_" + codigoLote + "/" + fileName;
                    var url = s3Util.uploadFile(s3Key, fileData);

                    var foto = new Foto();
                    foto.lote = lote;
                    foto.codigoLote = codigoLote;
                    foto.nombre = fileName;
                    foto.url = url;
                    foto.servicio = "S3";
                    foto.contentType = contentType;
                    foto.tamanioBytes = (long) fileData.length;

                    fotoRepository.persist(foto);
                    lote.addFoto(foto);

                    return ApiResponse.created("Archivo subido exitosamente", mapToDTO(foto));
                })
                .orElse(ApiResponse.error("Lote no encontrado con código: " + codigoLote));
    }

    public ApiResponse<byte[]> downloadFromS3(Long id) {
        return fotoRepository.findByIdOptional(id)
                .map(foto -> {
                    try {
                        var fileData = s3Util.getFile(foto.nombre);
                        return ApiResponse.success("Archivo descargado exitosamente", fileData);
                    } catch (Exception e) {
                        return ApiResponse.<byte[]>error("Error descargando archivo: " + e.getMessage());
                    }
                })
                .orElse(ApiResponse.notFound("Foto no encontrada con ID: " + id));
    }

     */

    private Foto mapToEntity(FotoDTO dto) {
        var f = new Foto();
        f.codigoLote = dto.codigoLote();
        f.servicio = dto.servicio() != null ? dto.servicio() : "S3";
        f.nombre = dto.nombre();
        f.url = dto.url();
        f.tipoTerreno = dto.tipoTerreno();
        f.tipoFoto = dto.tipoFoto();
        f.contentType = dto.contentType();
        f.tamanioBytes = dto.tamanioBytes();
        return f;
    }

    private FotoDTO mapToDTO(Foto e) {
        return new FotoDTO(
                e.id,
                e.lote != null ? e.lote.id : null,
                e.codigoLote,
                e.servicio,
                e.nombre,
                e.url,
                e.tipoTerreno,
                e.tipoFoto,
                e.contentType,
                e.tamanioBytes
        );
    }
}
