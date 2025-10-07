package com.municipalidad.catastro.handler;

import com.municipalidad.catastro.model.Estimacion;
import com.municipalidad.catastro.service.EstimacionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/estimacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstimacionHandler {

    @Inject
    EstimacionService service;

    @POST
    public Response create(Estimacion estimacion) {
        try {
            validateEstimacion(estimacion);
            Estimacion created = service.create(estimacion);
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
                            "Error creating estimacion: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        try {
            return service.getById(id)
                    .map(estimacion -> Response.ok(estimacion).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(new ErrorResponse("Not Found", 404,
                                    "Estimacion not found with id: " + id))
                            .build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error retrieving estimacion: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAll(@QueryParam("codigoLote") String codigoLote) {
        try {
            List<Estimacion> estimaciones;

            if (codigoLote != null && !codigoLote.trim().isEmpty()) {
                estimaciones = service.getByCodigoLote(codigoLote);
            } else {
                estimaciones = service.getAll();
            }

            return Response.ok(estimaciones).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Validation Error", 400, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error retrieving estimaciones: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Estimacion estimacion) {
        try {
            validateEstimacion(estimacion);
            Estimacion updated = service.update(id, estimacion);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found", 404, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error updating estimacion: " + e.getMessage()))
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
                            "Estimacion not found with id: " + id))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal Server Error", 500,
                            "Error deleting estimacion: " + e.getMessage()))
                    .build();
        }
    }

    private void validateEstimacion(Estimacion estimacion) {
        if (estimacion == null) {
            throw new IllegalArgumentException("Estimacion cannot be null");
        }
        if (estimacion.codigoLote() == null || estimacion.codigoLote().trim().isEmpty()) {
            throw new IllegalArgumentException("Codigo lote is required");
        }
        if (!estimacion.codigoLote().matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Codigo lote must be exactly 8 digits");
        }
    }
}
