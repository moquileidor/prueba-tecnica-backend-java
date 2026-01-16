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
@Schema(description = "Request para resetear contraseña")
public class ResetPasswordRequest {
    
    @NotBlank(message = "El token es requerido")
    @Schema(description = "Token de recuperación", example = "abc123xyz")
    private String token;
    
    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Nueva contraseña", example = "newpassword123")
    private String newPassword;
}
