package com.universidad.servicioo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Escribe tu usuario.")
        @Size(max = 60, message = "El usuario es demasiado largo.")
        String username,
        @NotBlank(message = "Escribe tu contraseña.")
        @Size(max = 120, message = "La contraseña es demasiado larga.")
        String password
) {}
