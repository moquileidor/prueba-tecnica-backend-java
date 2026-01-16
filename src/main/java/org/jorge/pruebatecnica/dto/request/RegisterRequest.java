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
@Schema(description = "Request para registro de usuario")
public class RegisterRequest {
    
    @NotBlank(message = "El nombre completo es requerido")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String fullName;
    
    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico debe ser válido")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String email;
}
