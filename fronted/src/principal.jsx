// Punto de entrada de la capa de presentación; monta React e importa los estilos globales.
// Activa verificaciones adicionales de React durante el desarrollo.
import { StrictMode } from 'react'
// Obtiene la función que conecta la aplicación React con el DOM del navegador.
import { createRoot } from 'react-dom/client'
// Carga Bootstrap antes de los estilos propios para que estos puedan personalizarlo.
import 'bootstrap/dist/css/bootstrap.min.css'
// Aplica los estilos globales del sistema.
import './estilos/estilosBase.css'
// Importa el componente que contiene las pantallas y el estado principal.
import Aplicacion from './presentacion/Aplicacion.jsx'

// Busca el contenedor declarado en index.html y monta allí la interfaz.
createRoot(document.getElementById('root')).render(
  // StrictMode ayuda a detectar usos problemáticos de React mientras se desarrolla.
  <StrictMode>
    {/* Renderiza la aplicación completa dentro del elemento raíz. */}
    <Aplicacion />
  </StrictMode>,
)
