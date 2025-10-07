package com.municipalidad.catastro.service;

import com.municipalidad.catastro.model.Foto;
import com.municipalidad.catastro.repository.FotoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@ApplicationScoped
public class FotoService {

    @Inject
    FotoRepository repository;

    @Inject
    S3Client s3Client;

    private static final String BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_PHOTOS", "catastro-photos");

    /**
     * Creates a new foto with S3 upload if needed
     */
    public Foto create(Foto foto) {
        return Optional.ofNullable(foto)
                .map(uploadToS3IfNeeded())
                .map(repository::save)
                .orElseThrow(() -> new IllegalArgumentException("Foto cannot be null"));
    }

    /**
     * Retrieves a foto by ID
     */
    public Optional<Foto> getById(Integer id) {
        return repository.findById(id);
    }

    /**
     * Retrieves all fotos
     */
    public List<Foto> getAll() {
        return repository.findAll();
    }

    /**
     * Retrieves fotos by codigo lote
     */
    public List<Foto> getByCodigoLote(String codigoLote) {
        if (codigoLote == null || codigoLote.trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo lote cannot be null or empty");
        }
        if (!codigoLote.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Codigo lote must be exactly 8 digits");
        }
        return repository.findByCodigoLote(codigoLote);
    }

    /**
     * Updates an existing foto with S3 upload if needed
     */
    public Foto update(Integer id, Foto foto) {
        return repository.findById(id)
                .map(existing -> uploadToS3IfNeeded().apply(foto))
                .map(updated -> repository.update(id, updated))
                .orElseThrow(() -> new IllegalArgumentException("Foto not found with id: " + id));
    }

    /**
     * Deletes a foto by ID and removes from S3 if applicable
     */
    public boolean delete(Integer id) {
        return repository.findById(id)
                .map(foto -> {
                    deleteFromS3IfNeeded(foto);
                    return repository.delete(id);
                })
                .orElse(false);
    }

    // Functional helper methods

    private UnaryOperator<Foto> uploadToS3IfNeeded() {
        return foto -> {
            if (shouldUploadToS3(foto)) {
                String s3Key = generateS3Key(foto);
                performS3Upload(s3Key, foto.url());
                String s3Url = buildS3Url(s3Key);
                return foto.withUrl(s3Url);
            }
            return foto;
        };
    }

    private boolean shouldUploadToS3(Foto foto) {
        return foto.url() != null
                && !foto.url().trim().isEmpty()
                && !foto.url().startsWith("http");
    }

    private String generateS3Key(Foto foto) {
        return String.format("lotes/%s/%s_%d.jpg",
                foto.codigoLote(),
                Optional.ofNullable(foto.tipoTerreno()).orElse("general"),
                System.currentTimeMillis());
    }

    private void performS3Upload(String key, String content) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromString(content));
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload to S3: " + e.getMessage(), e);
        }
    }

    private String buildS3Url(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", BUCKET_NAME, key);
    }

    private void deleteFromS3IfNeeded(Foto foto) {
        if (foto.url() != null && foto.url().contains(BUCKET_NAME)) {
            try {
                String key = extractS3Key(foto.url());
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .build();
                s3Client.deleteObject(deleteRequest);
            } catch (Exception e) {
                // Log error but don't fail the delete operation
                System.err.println("Warning: Failed to delete from S3: " + e.getMessage());
            }
        }
    }

    private String extractS3Key(String url) {
        int keyStartIndex = url.indexOf(".com/") + 5;
        return keyStartIndex > 4 ? url.substring(keyStartIndex) : "";
    }
}
