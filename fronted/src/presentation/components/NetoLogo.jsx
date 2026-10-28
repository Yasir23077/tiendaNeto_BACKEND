// Logotipo NETO construido con texto y CSS para mostrarlo claro u oscuro.
function NetoLogo({ light = false }) {
  return <span className={`neto-logo ${light ? 'neto-logo-light' : ''}`} role="img" aria-label="Neto">
    <span>Neto</span><i /><b />
  </span>
}

export default NetoLogo
