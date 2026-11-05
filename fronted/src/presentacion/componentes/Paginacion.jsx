// Controles reutilizables para mostrar una pagina de hasta diez registros.
// Función: Componente que controla el cambio entre páginas de una lista.
export default function Paginacion({ total, pagina, onCambiar, tamano = 10, etiqueta = 'registros' }) {
  // Siempre muestra como minimo la pagina 1, incluso cuando no hay filas.
  const totalPaginas = Math.max(1, Math.ceil(total / tamano))
  // Evita que la pagina actual quede fuera de rango si se filtran o borran filas.
  const paginaActual = Math.min(pagina, totalPaginas)
  // Calcula el primer registro visible; para cero resultados muestra 0.
  const desde = total === 0 ? 0 : (paginaActual - 1) * tamano + 1
  // Calcula el ultimo registro visible sin superar el total.
  const hasta = Math.min(paginaActual * tamano, total)

  // Presenta el rango visible y botones que notifican el cambio al componente padre.
  return (
    <nav className="history-pagination" aria-label={`Paginacion de ${etiqueta}`}>
      <span>Mostrando {desde}–{hasta} de {total} {etiqueta}</span>
      <div>
        <button className="btn btn-outline-secondary btn-sm" type="button" disabled={paginaActual <= 1} onClick={() => onCambiar(paginaActual - 1)}>Anterior</button>
        <span>Pagina {paginaActual} de {totalPaginas}</span>
        <button className="btn btn-outline-secondary btn-sm" type="button" disabled={paginaActual >= totalPaginas} onClick={() => onCambiar(paginaActual + 1)}>Siguiente</button>
      </div>
    </nav>
  )
}
