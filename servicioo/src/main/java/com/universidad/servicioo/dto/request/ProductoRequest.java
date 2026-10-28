package com.universidad.servicioo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Campos permitidos para crear o modificar un producto del catálogo. */
public record ProductoRequest(
        @NotBlank(message = "El código es obligatorio.")
        @Size(max = 40, message = "El código no puede superar 40 caracteres.")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 160, message = "El nombre no puede superar 160 caracteres.")
        String nombre,

        @Size(max = 80, message = "La categoría no puede superar 80 caracteres.")
        String categoria,

        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo.")
        @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales.")
        BigDecimal precio
) {}
