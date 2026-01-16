package org.jorge.pruebatecnica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para inicio de sesión")
public class LoginRequest {
    
    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico debe ser válido")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String email;
    
    @NotBlank(message = "La contraseña es requerida")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}
