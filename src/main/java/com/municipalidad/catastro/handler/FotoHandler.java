package com.municipalidad.catastro.handler;

import com.municipalidad.catastro.model.Foto;
import com.municipalidad.catastro.service.FotoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/foto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FotoHandler {

    @Inject
    FotoService service;

    @POST
    public Response create(Foto foto) {
        try {
            validateFoto(foto);
            Foto created = service.create(foto);
            return Response.status(Response.Status.CREATED)
                    .entity(created)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Validation Error", 400, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error creating foto: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        try {
            return service.getById(id)
                    .map(foto -> Response.ok(foto).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Not Found", 404,
                                    "Foto not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error retrieving foto: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAll(@QueryParam("codigoLote") String codigoLote) {
        try {
            List<Foto> fotos;

            if (codigoLote != null && !codigoLote.trim().isEmpty()) {
                fotos = service.getByCodigoLote(codigoLote);
            } else {
                fotos = service.getAll();
            }

            return Response.ok(fotos).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Validation Error", 400, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error retrieving fotos: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Foto foto) {
        try {
            validateFoto(foto);
            Foto updated = service.update(id, foto);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found", 404, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error updating foto: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        try {
            boolean deleted = service.delete(id);
            return deleted
                    ? Response.noContent().build()
                    : Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found", 404,
                            "Foto not found with id: " + id))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error deleting foto: " + e.getMessage()))
                    .build();
        }
    }

    private void validateFoto(Foto foto) {
        if (foto == null) {
            throw new IllegalArgumentException("Foto cannot be null");
        }
        if (foto.codigoLote() == null || foto.codigoLote().trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo lote is required");
        }
        if (!foto.codigoLote().matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Codigo lote must be exactly 8 digits");
        }
        if (foto.nombre() == null || foto.nombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre is required");
        }
    }
}
