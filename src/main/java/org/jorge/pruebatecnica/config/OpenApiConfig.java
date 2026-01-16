package org.jorge.pruebatecnica.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Gestión de Usuarios",
        version = "1.0",
        description = "API REST para gestión de usuarios con autenticación JWT. " +
                     "Incluye registro de usuarios, configuración de contraseña, " +
                     "inicio de sesión y recuperación de contraseña.",
        contact = @Contact(
            name = "Jorge",
            email = "soporte@example.com"
        )
    ),
    servers = {
        @Server(
            description = "Servidor Local",
            url = "http://localhost:8080"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "Autenticación JWT. Obtén el token mediante el endpoint /api/auth/login",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
