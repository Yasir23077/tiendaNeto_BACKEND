// Logotipo NETO construido con texto y CSS para mostrarlo claro u oscuro.
// Función: Dibuja el logotipo NETO.
function LogoNeto({ light = false }) {
  // La clase opcional cambia el color para usar el logotipo sobre fondos oscuros.
  return <span className={`neto-logo ${light ? 'neto-logo-light' : ''}`} role="img" aria-label="Neto">
    <span>Neto</span><i /><b />
  </span>
}

export default LogoNeto
