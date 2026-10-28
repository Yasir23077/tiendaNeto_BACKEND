package com.universidad.servicioo.dto.response;

import com.universidad.servicioo.model.Venta;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Representación pública de una venta; excluye detalles internos de persistencia. */
public record VentaResponse(Long id, String producto, Integer cantidad, BigDecimal precioUnitario,
                            BigDecimal total, LocalDateTime fecha, String sucursal) {
    public static VentaResponse desde(Venta venta) {
        return new VentaResponse(venta.getId(), venta.getProducto(), venta.getCantidad(),
                venta.getPrecioUnitario(), venta.getTotal(), venta.getFecha(), venta.getSucursal());
    }
}
