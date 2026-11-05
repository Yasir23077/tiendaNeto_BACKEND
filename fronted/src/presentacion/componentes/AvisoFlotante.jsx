// Archivo: src/presentacion/componentes/AvisoFlotante.jsx. Funcion: Muestra mensajes de exito o error en una ventana flotante.
import { useEffect } from 'react'

// Función: Componente que enseña al usuario un mensaje inmediato de éxito o error.
export default function AvisoFlotante({ mensaje, onCerrar }) {
  // Inicia el cierre automático cuando aparece un aviso y limpia el temporizador al cambiar.
  useEffect(() => {
    if (!mensaje) return undefined
    const tiempo = mensaje.error ? 8000 : 4500
    const temporizador = window.setTimeout(() => onCerrar(null), tiempo)
    return () => window.clearTimeout(temporizador)
  }, [mensaje, onCerrar])

  // No crea elementos visuales cuando el componente no tiene un mensaje activo.
  if (!mensaje) return null
  // Selecciona el aspecto y la prioridad accesible según el tipo de resultado.
  const esError = Boolean(mensaje.error)

  return (
    <div className={`aviso-flotante ${esError ? 'aviso-error' : 'aviso-exito'}`}
      role={esError ? 'alert' : 'status'} aria-live={esError ? 'assertive' : 'polite'}>
      <span className="aviso-icono" aria-hidden="true">{esError ? '!' : '✓'}</span>
      <div className="aviso-contenido">
        <strong>{esError ? 'Ocurrió un problema' : 'Operación completada'}</strong>
        <p>{mensaje.texto}</p>
      </div>
      <button className="aviso-cerrar" type="button" aria-label="Cerrar aviso" onClick={() => onCerrar(null)}>×</button>
      <span className="aviso-tiempo" />
    </div>
  )
}
