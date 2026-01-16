package org.jorge.pruebatecnica.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para establecer contraseña inicial")
public class SetPasswordRequest {
    
    @NotBlank(message = "El token es requerido")
    @Schema(description = "Token de registro", example = "abc123xyz")
    private String token;
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Nueva contraseña", example = "password123")
    private String password;
}
