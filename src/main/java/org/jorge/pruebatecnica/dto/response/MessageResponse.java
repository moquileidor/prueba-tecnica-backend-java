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
@Schema(description = "Response genérico con mensaje")
public class MessageResponse {
    
    @Schema(description = "Mensaje de respuesta", example = "Operación exitosa")
    private String message;
}
