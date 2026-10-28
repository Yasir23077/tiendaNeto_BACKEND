import { useEffect } from 'react'

export default function AvisoFlotante({ mensaje, onCerrar }) {
  useEffect(() => {
    if (!mensaje) return undefined
    const tiempo = mensaje.error ? 8000 : 4500
    const temporizador = window.setTimeout(() => onCerrar(null), tiempo)
    return () => window.clearTimeout(temporizador)
  }, [mensaje, onCerrar])

  if (!mensaje) return null
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
