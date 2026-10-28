package com.universidad.servicioo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Datos que el cliente puede enviar para registrar o actualizar una venta. */
public record VentaRequest(
        @NotBlank(message = "El producto es obligatorio.")
        @Size(max = 255, message = "El nombre del producto no puede superar 255 caracteres.")
        String producto,

        @NotNull(message = "La cantidad es obligatoria.")
        @Positive(message = "La cantidad debe ser mayor que cero.")
        Integer cantidad,

        @NotNull(message = "El precio unitario es obligatorio.")
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo.")
        @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 enteros y 2 decimales.")
        BigDecimal precioUnitario,

        @NotBlank(message = "La sucursal es obligatoria.")
        @Size(max = 140, message = "El nombre de sucursal no puede superar 140 caracteres.")
        String sucursal
) {}
