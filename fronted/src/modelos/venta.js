/**
 * Modelo de dominio para las operaciones de venta del frontend.
 * Mantiene el formato del objeto y el cálculo del total en un solo lugar.
 */

/** Convierte valores del formulario a los tipos que espera el backend. */
// Función: Limpia y convierte los datos de venta a los tipos que espera el servidor.
export function normalizarVenta({ producto, cantidad, precioUnitario, sucursal }) {
  return {
    // El nombre se recorta para evitar espacios ingresados al principio o final.
    producto: producto.trim(),
    // La API recibe cantidad y precio como números, aunque los inputs sean texto.
    cantidad: Number(cantidad),
    precioUnitario: Number(precioUnitario),
    sucursal,
  }
}

/** Calcula el total como texto decimal de dos posiciones para el campo visual. */
// Función: Multiplica cantidad por precio y devuelve dos decimales.
export function calcularTotalVenta(cantidad, precioUnitario) {
  return (Number(cantidad) * Number(precioUnitario || 0)).toFixed(2)
}
