package org.jorge.pruebatecnica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jorge.pruebatecnica.dto.request.*;
import org.jorge.pruebatecnica.dto.response.AuthResponse;
import org.jorge.pruebatecnica.dto.response.MessageResponse;
import org.jorge.pruebatecnica.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para gestión de autenticación y sesión")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", 
               description = "Registra un nuevo usuario en el sistema y envía un correo electrónico con un enlace para configurar la contraseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "El correo electrónico ya está registrado")
    })
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        MessageResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/set-password")
    @Operation(summary = "Configurar contraseña inicial",
               description = "Permite al usuario configurar su contraseña usando el token recibido por correo electrónico. El token solo puede usarse una vez.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña configurada exitosamente",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Token inválido, expirado o ya utilizado")
    })
    public ResponseEntity<MessageResponse> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        MessageResponse response = userService.setPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Autentica al usuario y retorna un token JWT si las credenciales son válidas y el usuario está activo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "403", description = "Usuario inactivo")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperación de contraseña",
               description = "Envía un correo electrónico con un enlace para restablecer la contraseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correo de recuperación enviado",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        MessageResponse response = userService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Restablecer contraseña",
               description = "Permite al usuario restablecer su contraseña usando el token de recuperación. El token solo puede usarse una vez.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña restablecida exitosamente",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Token inválido, expirado o ya utilizado")
    })
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        MessageResponse response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
