// Pantalla de acceso; valida las cuentas de demostración antes de entrar al panel.
import { useEffect, useState } from 'react'
import { iniciarSesion as autenticar } from '../../services/autenticacionService'
import NetoLogo from './NetoLogo'

function Login({ onLogin, mensajeInicial = '' }) {
  // Valores ligados a los campos y texto de error del formulario de acceso.
  const [usuario, setUsuario] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [error, setError] = useState('')
  const [cargando, setCargando] = useState(false)
  useEffect(() => { if (mensajeInicial) setError(mensajeInicial) }, [mensajeInicial])

  // Comprueba las credenciales y entrega el usuario válido al componente raíz.
  const entrar = async (evento) => {
    evento.preventDefault()
    setError('')
    setCargando(true)
    try {
      const perfil = await autenticar({ username: usuario.trim().toLowerCase(), password: contrasena })
      onLogin(perfil)
    } catch (fallo) {
      setError(fallo.message || 'No se pudo iniciar sesión. Revisa tus datos e intenta otra vez.')
    } finally {
      setCargando(false)
    }
  }

  return (
    <main className="login-page">
      {/* Panel de marca y resumen visual mostrado en pantallas amplias. */}
      <section className="login-showcase">
        <div className="login-showcase-top"><NetoLogo light /><span className="showcase-brand-caption">SISTEMA DE TIENDA</span></div>
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
          <div className="login-mobile-brand"><NetoLogo /><span>SISTEMA DE TIENDA</span></div>
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
              <input id="contrasena" type="password" autoComplete="current-password" value={contrasena} onChange={(e) => setContrasena(e.target.value)} placeholder="Escribe tu contraseña" required />
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

export default Login
