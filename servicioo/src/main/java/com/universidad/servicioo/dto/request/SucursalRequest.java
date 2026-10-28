package com.universidad.servicioo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos permitidos para registrar una sucursal. */
public record SucursalRequest(
        @NotBlank(message = "El código es obligatorio.")
        @Size(max = 30, message = "El código no puede superar 30 caracteres.")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 140, message = "El nombre no puede superar 140 caracteres.")
        String nombre
) {}
