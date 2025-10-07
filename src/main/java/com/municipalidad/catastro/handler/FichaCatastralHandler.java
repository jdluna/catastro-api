package com.municipalidad.catastro.handler;

import com.municipalidad.catastro.model.FichaCatastral;
import com.municipalidad.catastro.service.FichaCatastralService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/ficha-catastral")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FichaCatastralHandler {

    @Inject
    FichaCatastralService service;

    @POST
    public Response create(FichaCatastral ficha) {
        try {
            validateFichaCatastral(ficha);
            FichaCatastral created = service.create(ficha);
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
                            "Error creating ficha catastral: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        try {
            return service.getById(id)
                    .map(ficha -> Response.ok(ficha).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Not Found", 404,
                                    "Ficha catastral not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error retrieving ficha catastral: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAll(
            @QueryParam("ubigeo") String ubigeo,
            @QueryParam("sector") String sector,
            @QueryParam("manzana") String manzana,
            @QueryParam("lote") String lote) {
        try {
            List<FichaCatastral> fichas;

            if (sector != null && manzana != null && lote != null) {
                fichas = service.getBySectorManzanaLote(sector, manzana, lote);
            } else if (ubigeo != null && !ubigeo.trim().isEmpty()) {
                fichas = service.getByUbigeo(ubigeo);
            } else {
                fichas = service.getAll();
            }

            return Response.ok(fichas).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Validation Error", 400, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error retrieving fichas catastrales: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, FichaCatastral ficha) {
        try {
            validateFichaCatastral(ficha);
            FichaCatastral updated = service.update(id, ficha);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found", 404, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error updating ficha catastral: " + e.getMessage()))
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
                            "Ficha catastral not found with id: " + id))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error deleting ficha catastral: " + e.getMessage()))
                    .build();
        }
    }

    private void validateFichaCatastral(FichaCatastral ficha) {
        if (ficha == null) {
            throw new IllegalArgumentException("Ficha catastral cannot be null");
        }
        if (ficha.sector() == null || ficha.sector().trim().isEmpty()) {
            throw new IllegalArgumentException("Sector is required");
        }
        if (ficha.manzana() == null || ficha.manzana().trim().isEmpty()) {
            throw new IllegalArgumentException("Manzana is required");
        }
        if (ficha.lote() == null || ficha.lote().trim().isEmpty()) {
            throw new IllegalArgumentException("Lote is required");
        }
    }
}
