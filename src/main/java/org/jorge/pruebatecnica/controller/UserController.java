package org.jorge.pruebatecnica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jorge.pruebatecnica.dto.response.UserResponse;
import org.jorge.pruebatecnica.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios (requiere autenticación)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios",
               description = "Retorna una lista de todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT requerido")
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID",
               description = "Retorna la información de un usuario específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado - Token JWT requerido"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
