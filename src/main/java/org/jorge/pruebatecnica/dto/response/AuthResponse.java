package org.jorge.pruebatecnica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response de autenticación con token JWT")
public class AuthResponse {
    
    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Correo electrónico del usuario", example = "jorge@gmail.com")
    private String email;
    
    @Schema(description = "Nombre completo del usuario", example = "Jorge Alvarado")
    private String fullName;
}
