package com.municipalidad.catastro.handler;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(
                    "Bad Request",
                    400,
                    exception.getMessage()
                ))
                .build();
        }

        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(
                    "Not Found",
                    404,
                    exception.getMessage()
                ))
                .build();
        }

        // Log internal errors but don't expose details
        System.err.println("Internal error: " + exception.getMessage());
        exception.printStackTrace();

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ErrorResponse(
                "Internal Server Error",
                500,
                "An unexpected error occurred"
            ))
            .build();
    }
}
