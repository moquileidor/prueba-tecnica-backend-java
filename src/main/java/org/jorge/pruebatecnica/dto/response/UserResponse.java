package org.jorge.pruebatecnica.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response con información del usuario")
public class UserResponse {
    
    @Schema(description = "ID del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo", example = "Jorge Alvarado")
    private String fullName;
    
    @Schema(description = "Correo electrónico", example = "jorge@gmail.com")
    private String email;
    
    @Schema(description = "Estado del usuario", example = "true")
    private boolean active;
    
    @Schema(description = "Fecha de creación")
    private LocalDateTime createdAt;
}
