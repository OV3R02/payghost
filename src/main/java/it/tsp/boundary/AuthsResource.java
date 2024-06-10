package it.tsp.boundary;

import it.tsp.control.PayghostManager;
import it.tsp.dto.CredentialDTO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/auths")
public class AuthsResource {


    @Inject
    PayghostManager payghostManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid CredentialDTO e){
        String id = payghostManager.doLogin(e);
        JsonObject result = Json.createObjectBuilder().add("id", id).build();
        return Response.ok(result).build();
    } 

}
