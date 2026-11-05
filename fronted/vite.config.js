// Configura Vite con React y reenvía las solicitudes /api al servidor Spring.
// Agrega a Vite la transformación necesaria para componentes React y JSX.
import react from '@vitejs/plugin-react'
// Importa el helper para declarar la configuración del servidor de desarrollo.
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  // Registra el complemento de React en el proceso de compilación.
  plugins: [react()],
  server: {
    // Acepta el dominio HTTPS específico que ngrok reenvía al servidor de Vite.
    allowedHosts: ['proved-revolving-oink.ngrok-free.dev'],
    proxy: {
      // Reenvía las llamadas del navegador al API local de Spring Boot.
      '/api': 'http://localhost:8080',
    },
  },
})
