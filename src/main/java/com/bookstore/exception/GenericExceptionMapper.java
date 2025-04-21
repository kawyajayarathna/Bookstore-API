package com.bookstore.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
        public Response toResponse(RuntimeException exception) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }


