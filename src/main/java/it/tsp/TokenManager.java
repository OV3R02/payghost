package it.tsp;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.smallrye.jwt.build.Jwt;
import it.tsp.entity.Account;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class TokenManager {

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String iss;

    public String generate(Account e) {
        String token = Jwt.issuer(iss)
            .subject(String.valueOf(e.getID()))
            .upn(e.getEmail())
            .groups(new HashSet<>(Arrays.asList("USERS")))
            .expiresIn(Duration.ofMinutes(60))
            .sign();
        return token;
    }
}
