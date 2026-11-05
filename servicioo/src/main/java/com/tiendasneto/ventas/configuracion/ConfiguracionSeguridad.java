// Archivo: src/main/java/com/tiendasneto/ventas/configuracion/ConfiguracionSeguridad.java. Funcion: Configura login, sesion, CSRF y permisos por rol.
// Declara el paquete com.tiendasneto.ventas.configuracion al que pertenece esta clase.
package com.tiendasneto.ventas.configuracion;

// Importa jakarta.servlet.http.HttpServletResponse para poder usar ese tipo o paquete en este archivo.
import jakarta.servlet.http.HttpServletResponse;
// Importa jakarta.servlet.http.HttpServletRequest para poder usar ese tipo o paquete en este archivo.
import jakarta.servlet.http.HttpServletRequest;
// Importa org.springframework.context.annotation.Bean para poder usar ese tipo o paquete en este archivo.
import org.springframework.context.annotation.Bean;
// Importa org.springframework.context.annotation.Configuration para poder usar ese tipo o paquete en este archivo.
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
// Importa org.springframework.http.HttpMethod para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.HttpMethod;
// Importa org.springframework.http.MediaType para poder usar ese tipo o paquete en este archivo.
import org.springframework.http.MediaType;
// Importa org.springframework.security.authentication.AuthenticationManager para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.authentication.AuthenticationManager;
// Importa org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// Importa org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// Importa org.springframework.security.config.annotation.web.builders.HttpSecurity para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Permite activar la configuración CORS declarada como bean.
import org.springframework.security.config.Customizer;
// Importa org.springframework.security.config.http.SessionCreationPolicy para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.config.http.SessionCreationPolicy;
// Importa org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Importa org.springframework.security.crypto.password.PasswordEncoder para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.crypto.password.PasswordEncoder;
// Importa org.springframework.security.web.SecurityFilterChain para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.SecurityFilterChain;
// Importa org.springframework.security.web.context.HttpSessionSecurityContextRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
// Importa org.springframework.security.web.context.SecurityContextRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.context.SecurityContextRepository;
// Importa org.springframework.security.web.csrf.CookieCsrfTokenRepository para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
// Importa org.springframework.security.web.csrf.CsrfToken para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.csrf.CsrfToken;
// Importa org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
// Importa org.springframework.security.web.csrf.CsrfTokenRequestHandler para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
// Importa org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler para poder usar ese tipo o paquete en este archivo.
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
// Define los orígenes, métodos y encabezados permitidos por CORS.
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// Proporciona la lista inmutable de valores de configuración.
import java.util.List;
// Importa java.util.function.Supplier para poder usar ese tipo o paquete en este archivo.
import java.util.function.Supplier;

// Declara beans de configuracion que Spring carga al iniciar.
@Configuration
// Activa anotaciones de autorizacion como @PreAuthorize en los controladores.
@EnableMethodSecurity
// Declara class ConfiguracionSeguridad y define su bloque de implementación.
public class ConfiguracionSeguridad {
    // Configura BCrypt para guardar contrasenas como hashes y verificarlas al autenticar.
    @Bean
    // Continúa la expresión o declaración iniciada en las líneas cercanas.
    PasswordEncoder passwordEncoder() {
        // Devuelve el resultado calculado por este método o bloque.
        return new BCryptPasswordEncoder();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Expone el administrador que valida las credenciales del inicio de sesion.
    @Bean
    // Continúa la expresión o declaración iniciada en las líneas cercanas.
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Devuelve el resultado calculado por este método o bloque.
        return configuration.getAuthenticationManager();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Conserva la identidad autenticada en la sesion HTTP entre peticiones.
    @Bean
    // Continúa la expresión o declaración iniciada en las líneas cercanas.
    SecurityContextRepository securityContextRepository() {
        // Devuelve el resultado calculado por este método o bloque.
        return new HttpSessionSecurityContextRepository();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Guarda el token CSRF en una cookie que el frontend puede leer y reenviar.
    @Bean
    // Continúa la expresión o declaración iniciada en las líneas cercanas.
    CookieCsrfTokenRepository csrfTokenRepository() {
        // Devuelve el resultado calculado por este método o bloque.
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    // Cierra el bloque o la expresión que se definió anteriormente.
    }

    // Autoriza únicamente los orígenes explícitos usados por el frontend.
    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${app.frontend-origin:http://localhost:5173}") String frontendOrigin) {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of(frontendOrigin));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", cors);
        return source;
    }

    // Define la cadena de filtros que protege las solicitudes HTTP.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            CookieCsrfTokenRepository csrfRepository,
            // Continúa la expresión o declaración iniciada en las líneas cercanas.
            SecurityContextRepository securityContextRepository) throws Exception {
        // Continúa la expresión o declaración iniciada en las líneas cercanas.
        http
            .cors(Customizer.withDefaults())
            // Valida CSRF en solicitudes que modifican informacion.
            .csrf(csrf -> csrf
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .csrfTokenRepository(csrfRepository)
                // Acepta el token CSRF sin enmascarar que el frontend lee de la cookie XSRF-TOKEN.
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
            // Guarda y recupera la autenticacion desde la sesion.
            .securityContext(context -> context.securityContextRepository(securityContextRepository))
            // Crea una sesion solo cuando una peticion necesita conservar el login.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            // Devuelve JSON claro cuando falta autenticacion o autorizacion.
            .exceptionHandling(errors -> errors
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .authenticationEntryPoint((request, response, exception) -> {
                    // 401 significa que no hay un usuario autenticado.
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    // Ejecuta esta instrucción de la lógica actual.
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    // Ejecuta esta instrucción de la lógica actual.
                    response.getWriter().write("{\"mensaje\":\"Inicia sesión para continuar.\"}");
                // Cierra el bloque o la expresión que se definió anteriormente.
                })
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .accessDeniedHandler((request, response, exception) -> {
                    // 403 significa que el usuario no tiene el rol necesario.
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    // Ejecuta esta instrucción de la lógica actual.
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    // Ejecuta esta instrucción de la lógica actual.
                    response.getWriter().write("{\"mensaje\":\"No tienes permisos para realizar esta acción.\"}");
                // Continúa la expresión o declaración iniciada en las líneas cercanas.
                }))
            // Establece permisos por metodo HTTP, ruta y rol.
            .authorizeHttpRequests(auth -> auth
                // Permite solicitar el token CSRF antes de iniciar sesion.
                .requestMatchers(HttpMethod.GET, "/api/auth/csrf").permitAll()
                // Permite iniciar sesion enviando credenciales.
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                // Exige una sesion para consultar el perfil y cerrar sesion.
                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                // Continúa la llamada encadenada sobre el objeto o resultado anterior.
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                // Cualquier perfil puede consultar ventas.
                .requestMatchers(HttpMethod.GET, "/api/ventas", "/api/ventas/**").hasAnyRole("CAJERO", "LIDER", "ENCARGADO")
                // Los tres roles pueden registrar ventas nuevas.
                .requestMatchers(HttpMethod.POST, "/api/ventas", "/api/ventas/lote").hasAnyRole("CAJERO", "LIDER", "ENCARGADO")
                // Solo lider y encargado pueden modificar o eliminar ventas existentes.
                .requestMatchers(HttpMethod.PUT, "/api/ventas/**").hasAnyRole("LIDER", "ENCARGADO")
                .requestMatchers(HttpMethod.DELETE, "/api/ventas/**").hasAnyRole("LIDER", "ENCARGADO")
                // Los tres perfiles pueden consultar productos y sucursales.
                .requestMatchers(HttpMethod.GET, "/api/productos", "/api/productos/**", "/api/sucursales").hasAnyRole("CAJERO", "LIDER", "ENCARGADO")
                // Solo lider y encargado pueden cambiar los catalogos.
                .requestMatchers(HttpMethod.POST, "/api/productos", "/api/sucursales").hasAnyRole("LIDER", "ENCARGADO")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyRole("LIDER", "ENCARGADO")
                // Solo encargado puede consultar la auditoria.
                .requestMatchers(HttpMethod.GET, "/api/auditoria").hasRole("ENCARGADO")
                // Solo el encargado administra y consulta las cuentas de usuario.
                .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ENCARGADO")
                .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ENCARGADO")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ENCARGADO")
                // Deniega cualquier endpoint que no se haya autorizado arriba.
                .anyRequest().denyAll());
        // Construye la cadena de filtros y la entrega a Spring Security.
        return http.build();
    }

    // Spring usa tokens enmascarados por defecto; para una SPA se acepta el token de cookie
    // enviado en el encabezado y se mantiene el manejo XOR para atributos de solicitud.
    private static final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
        // Handler simple para leer el token CSRF que envía el frontend en un encabezado.
        private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
        // Handler XOR que conserva el tratamiento estándar de Spring para sus atributos CSRF.
        private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

        @Override
        // Función: Prepara el token CSRF para la solicitud y fuerza la creación de su cookie.
        public void handle(HttpServletRequest request, HttpServletResponse response,
                Supplier<CsrfToken> deferredCsrfToken) {
            xor.handle(request, response, deferredCsrfToken);
            deferredCsrfToken.get();
        }

        @Override
        // Función: Interpreta el token CSRF que llega desde el navegador.
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            String headerValue = request.getHeader(csrfToken.getHeaderName());
            return (headerValue != null ? plain : xor).resolveCsrfTokenValue(request, csrfToken);
        }
    }
}
