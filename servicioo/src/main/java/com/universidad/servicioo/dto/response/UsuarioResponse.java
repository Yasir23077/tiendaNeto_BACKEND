package com.universidad.servicioo.dto.response;

import com.universidad.servicioo.model.UsuarioSistema;

public record UsuarioResponse(String username, String name, String role, String roleLabel, String initials) {
    public static UsuarioResponse desde(UsuarioSistema usuario) {
        String roleLabel = switch (usuario.getRol()) {
            case "ENCARGADO" -> "Encargado de tienda";
            case "LIDER" -> "Líder de tienda";
            default -> "Cajero";
        };
        String initials = java.util.Arrays.stream(usuario.getNombre().trim().split("\\s+"))
                .limit(2).map(palabra -> palabra.substring(0, 1).toUpperCase())
                .reduce("", String::concat);
        return new UsuarioResponse(usuario.getUsername(), usuario.getNombre(),
                usuario.getRol().toLowerCase(), roleLabel, initials);
    }
}
