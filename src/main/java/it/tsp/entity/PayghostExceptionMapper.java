package it.tsp.entity;

import it.tsp.boundary.PayghostException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PayghostExceptionMapper implements ExceptionMapper<PayghostException> {

    @Override
    public Response toResponse(PayghostException exception) {
        return Response.status(Status.BAD_REQUEST)
                .header("caused-by", exception.getMessage())
                .build();
    }

    
}
