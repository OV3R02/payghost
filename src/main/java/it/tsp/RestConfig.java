package it.tsp;

import org.eclipse.microprofile.auth.LoginConfig;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@LoginConfig(authMethod = "MP-JWT", realmName = "MP-JWT")
@DeclareRoles({"USERS"})
@ApplicationPath("/api")
public class RestConfig extends Application {

}
