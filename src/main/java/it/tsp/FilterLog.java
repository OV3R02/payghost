package it.tsp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FilterLog implements ContainerRequestFilter, ContainerResponseFilter {
    Logger logger = Logger.getLogger(this.getClass().getName());

    long startTime;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.startTime = System.nanoTime();
        
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
                long elapsedTime = System.nanoTime() - startTime;
                logger.log(Level.INFO,
                        String.format("process request to %s in %s nanosec", requestContext.getUriInfo().getPath(), elapsedTime));
        
    }

    

    

}
