package com.universidad.servicioo.dto.response;

import com.universidad.servicioo.model.Sucursal;

/** Campos de sucursal que la API entrega al frontend. */
public record SucursalResponse(Long id, String codigo, String nombre, boolean activa) {
    public static SucursalResponse desde(Sucursal sucursal) {
        return new SucursalResponse(sucursal.getId(), sucursal.getCodigo(), sucursal.getNombre(), sucursal.isActiva());
    }
}
