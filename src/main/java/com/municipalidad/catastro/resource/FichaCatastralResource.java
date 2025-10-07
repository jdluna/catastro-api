package com.municipalidad.catastro.resource;

import com.municipalidad.catastro.dto.ApiResponse;
import com.municipalidad.catastro.dto.FichaCatastralDTO;
import com.municipalidad.catastro.service.FichaCatastralService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/fichas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FichaCatastralResource {

    @Inject
    FichaCatastralService fichaService;

    // POST /api/fichas
    @POST
    public Response create(@Valid FichaCatastralDTO dto) {
        var response = fichaService.create(dto);
        return response.success()
                ? Response.status(Response.Status.CREATED).entity(response).build()
                : Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    // GET /api/fichas/{id}
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        var response = fichaService.findById(id);
        return response.success()
                ? Response.ok(response).build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // PUT /api/fichas/{id}
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid FichaCatastralDTO dto) {
        var response = fichaService.update(id, dto);
        return response.success()
                ? Response.ok(response).build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // DELETE /api/fichas/{id}
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        var response = fichaService.delete(id);
        return response.success()
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }

    // GET /api/fichas/lote/{codigoLote}
    @GET
    @Path("/lote/{codigoLote}")
    public Response findByCodigoLote(@PathParam("codigoLote") String codigoLote) {
        var response = fichaService.findByCodigoLote(codigoLote);
        return Response.ok(response).build();
    }
}
