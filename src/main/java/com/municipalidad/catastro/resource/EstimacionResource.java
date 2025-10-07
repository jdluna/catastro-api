package com.municipalidad.catastro.resource;

import com.municipalidad.catastro.dto.ApiResponse;
import com.municipalidad.catastro.dto.EstimacionDTO;
import com.municipalidad.catastro.service.EstimacionService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/estimaciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstimacionResource {

    @Inject
    EstimacionService estimacionService;

    // POST /api/estimaciones
    @POST
    public Response create(@Valid EstimacionDTO dto) {
        var response = estimacionService.create(dto);
        return response.success()
                ? Response.status(Response.Status.CREATED).entity(response).build()
                : Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    // GET /api/estimaciones/{id}
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        var response = estimacionService.findById(id);
        return response.success()
                ? Response.ok(response).build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // PUT /api/estimaciones/{id}
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid EstimacionDTO dto) {
        var response = estimacionService.update(id, dto);
        return response.success()
                ? Response.ok(response).build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // DELETE /api/estimaciones/{id}
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        var response = estimacionService.delete(id);
        return response.success()
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // GET /api/estimaciones/lote/{loteId}
    @GET
    @Path("/lote/{loteId}")
    public Response findByLoteId(@PathParam("loteId") Long loteId) {
        var response = estimacionService.findByLoteId(loteId);
        return Response.ok(response).build();
    }
}
