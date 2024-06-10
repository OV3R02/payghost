package it.tsp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CredentialDTO(@NotBlank @Email String email, @NotBlank String pwd) {
    
}
