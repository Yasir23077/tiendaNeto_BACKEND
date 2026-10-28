package com.universidad.servicioo.dto.response;

import com.universidad.servicioo.model.Producto;
import java.math.BigDecimal;

/** Campos de producto que la API entrega al frontend. */
public record ProductoResponse(Long id, String codigo, String nombre, String categoria,
                               BigDecimal precio, boolean activo) {
    public static ProductoResponse desde(Producto producto) {
        return new ProductoResponse(producto.getId(), producto.getCodigo(), producto.getNombre(),
                producto.getCategoria(), producto.getPrecio(), producto.isActivo());
    }
}
