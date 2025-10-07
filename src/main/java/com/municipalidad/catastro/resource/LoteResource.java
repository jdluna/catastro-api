package com.municipalidad.catastro.resource;

import com.municipalidad.catastro.dto.ApiResponse;
import com.municipalidad.catastro.dto.LoteDTO;
import com.municipalidad.catastro.service.LoteService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/lotes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoteResource {

    @Inject
    LoteService loteService;

    // POST /api/lotes
    @POST
    public Response create(@Valid LoteDTO dto) {
        var response = loteService.create(dto);
        return response.success()
                ? Response.status(Response.Status.CREATED).entity(response).build()
                : Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    // GET /api/lotes/{id}
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        var response = loteService.findById(id);
        return response.success()
                ? Response.ok(response).build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // PUT /api/lotes/{id}
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid LoteDTO dto) {
        var response = loteService.update(id, dto);
        return response.success()
                ? Response.ok(response).build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // DELETE /api/lotes/{id}
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        var response = loteService.delete(id);
        return response.success()
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // GET /api/lotes
    @GET
    public Response findAll(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sector") String codigoSector,
            @QueryParam("manzana") String codigoManzana) {

        // Si se especifica sector y manzana
        if (codigoSector != null && codigoManzana != null) {
            var response = loteService.findByManzana(codigoSector, codigoManzana);
            return Response.ok(response).build();
        }

        // Si solo se especifica sector
        if (codigoSector != null) {
            var response = loteService.findBySector(codigoSector);
            return Response.ok(response).build();
        }

        // Lista general con paginación
        var response = loteService.findAll(page, size);
        return Response.ok(response).build();
    }
}
