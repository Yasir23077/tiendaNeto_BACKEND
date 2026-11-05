// Pantalla de acceso; Spring Security valida las credenciales y establece la sesion.
import { useEffect, useState } from 'react'
import { iniciarSesion as autenticar } from '../../servicios/servicioAutenticacion'
import LogoNeto from './LogoNeto'

// Función: Dibuja el acceso y recoge las credenciales del usuario.
function InicioSesion({ onLogin, mensajeInicial = '' }) {
  // Valores ligados a los campos y texto de error del formulario de acceso.
  // Usuario enviado al backend para buscar una cuenta registrada.
  const [usuario, setUsuario] = useState('')
  // Contrasena temporal del formulario, no se guarda en el navegador.
  const [contrasena, setContrasena] = useState('')
  // Controla si el campo enseña u oculta los caracteres escritos.
  const [mostrarContrasena, setMostrarContrasena] = useState(false)
  // Error de credenciales o de conexion que se muestra a la persona.
  const [error, setError] = useState('')
  // Bloquea el envio mientras Spring valida los datos.
  const [cargando, setCargando] = useState(false)
  useEffect(() => { if (mensajeInicial) setError(mensajeInicial) }, [mensajeInicial])

  // Comprueba las credenciales y entrega el usuario válido al componente raíz.
  // Función: Envía las credenciales y comunica errores de acceso.
  const entrar = async (evento) => {
    // Evita la recarga tradicional del formulario HTML.
    evento.preventDefault()
    setError('')
    setCargando(true)
    try {
      // Envia las credenciales a Spring Security y recibe el perfil autenticado.
      const perfil = await autenticar({ username: usuario.trim().toLowerCase(), password: contrasena })
      // Pasa el perfil validado al componente raiz de la aplicacion.
      onLogin(perfil)
    } catch (fallo) {
      setError(fallo.message || 'No se pudo iniciar sesión. Revisa tus datos e intenta otra vez.')
    } finally {
      // Reactiva el formulario al terminar la solicitud.
      setCargando(false)
    }
  }

  return (
    <main className="login-page">
      {/* Panel de marca y resumen visual mostrado en pantallas amplias. */}
      <section className="login-showcase">
        <div className="login-showcase-top"><LogoNeto light /><span className="showcase-brand-caption">SISTEMA DE TIENDA</span></div>
        <div className="login-showcase-copy">
          <span className="showcase-kicker">SISTEMA DE OPERACIÓN</span>
          <h1>Todo listo para<br />una gran jornada.</h1>
          <p>Administra las ventas de tu sucursal de forma simple, clara y segura.</p>
          <div className="showcase-rule"><span /><span /><span /></div>
          <div className="showcase-caption"><span className="showcase-caption-icon">↗</span><span>Tu tienda, en movimiento.<br /><b>Tu equipo, conectado.</b></span></div>
        </div>
        <div className="showcase-watermark">NETO</div>
        <div className="showcase-orbit showcase-orbit-a" /><div className="showcase-orbit showcase-orbit-b" />
        <div className="showcase-footer"><span>NETO VENTAS</span><span>PLATAFORMA DE SUCURSAL</span></div>
      </section>

      {/* Formulario de acceso y mensajes de ayuda al usuario. */}
      <section className="login-panel-wrap">
        <div className="login-panel">
          <div className="login-mobile-brand"><LogoNeto /><span>SISTEMA DE TIENDA</span></div>
          <div className="login-heading">
            <span className="login-eyebrow">BIENVENIDO DE NUEVO</span>
            <h2>Inicia sesión</h2>
            <p>Ingresa con tu cuenta de sucursal para continuar.</p>
          </div>

          {/* Campos controlados de usuario y contraseña y botón de ingreso. */}
          <form onSubmit={entrar}>
            <label className="login-label" htmlFor="usuario">Usuario</label>
            <div className="login-input-wrap"><span className="login-input-icon">◉</span>
              <input id="usuario" autoComplete="username" value={usuario} onChange={(e) => setUsuario(e.target.value)} placeholder="Escribe tu usuario" required />
            </div>
            <div className="password-label-row"><label className="login-label" htmlFor="contrasena">Contraseña</label></div>
            <div className="login-input-wrap"><span className="login-input-icon">⌑</span>
              <input id="contrasena" type={mostrarContrasena ? 'text' : 'password'} autoComplete="current-password" value={contrasena} onChange={(e) => setContrasena(e.target.value)} placeholder="Escribe tu contraseña" required />
              <button className="login-password-toggle" type="button" onClick={() => setMostrarContrasena((visible) => !visible)} aria-label={mostrarContrasena ? 'Ocultar contraseña' : 'Mostrar contraseña'} aria-pressed={mostrarContrasena} title={mostrarContrasena ? 'Ocultar contraseña' : 'Mostrar contraseña'}>
                {mostrarContrasena
                  ? <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M3 3l18 18M10.6 10.7a2 2 0 0 0 2.7 2.7M9.9 5.2A10.8 10.8 0 0 1 12 5c5.2 0 8.8 4.4 10 7-.4 1-1.3 2.2-2.5 3.3M6.2 6.3C3.9 7.8 2.5 10 2 12c1.2 2.6 4.8 7 10 7 1.2 0 2.3-.2 3.3-.6" /></svg>
                  : <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7S2 12 2 12Z" /><circle cx="12" cy="12" r="3" /></svg>}
              </button>
            </div>
            {error && <div className="login-error" role="alert">{error}</div>}
            <button type="submit" className="login-submit" disabled={cargando}>{cargando ? 'Verificando…' : 'Entrar'} <span>{cargando ? '◌' : '→'}</span></button>
          </form>

          <div className="login-help"><span className="login-help-icon">i</span><span>El acceso se asigna por perfil de sucursal.<br />Si no tienes una cuenta, contacta al encargado de tienda.</span></div>
        </div>
        <footer className="login-copyright">© NETO Ventas · Panel de operación</footer>
      </section>
    </main>
  )
}

export default InicioSesion
