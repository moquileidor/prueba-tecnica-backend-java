package org.jorge.pruebatecnica.service.impl;

import org.jorge.pruebatecnica.dto.request.*;
import org.jorge.pruebatecnica.dto.response.AuthResponse;
import org.jorge.pruebatecnica.dto.response.MessageResponse;
import org.jorge.pruebatecnica.dto.response.UserResponse;
import org.jorge.pruebatecnica.entity.Token;
import org.jorge.pruebatecnica.entity.User;
import org.jorge.pruebatecnica.enums.TokenType;
import org.jorge.pruebatecnica.repository.TokenRepository;
import org.jorge.pruebatecnica.repository.UserRepository;
import org.jorge.pruebatecnica.security.JwtService;
import org.jorge.pruebatecnica.service.EmailService;
import org.jorge.pruebatecnica.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtService jwtService,
            @Lazy AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    @Override
    @Transactional
    public MessageResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        // Crear usuario
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .active(false)
                .build();

        user = userRepository.save(user);

        // Generar token
        String tokenValue = UUID.randomUUID().toString();
        Token token = Token.builder()
                .token(tokenValue)
                .type(TokenType.REGISTRATION)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();

        tokenRepository.save(token);

        // Enviar correo
        try {
            emailService.sendRegistrationEmail(user.getEmail(), user.getFullName(), tokenValue);
            return MessageResponse.builder()
                    .message("Registro exitoso. Se ha enviado un correo electrónico para configurar tu contraseña.")
                    .build();
        } catch (Exception e) {
            // Si falla el envío del email, igual se completó el registro
            return MessageResponse.builder()
                    .message("Registro exitoso. NOTA: No se pudo enviar el correo. Token de configuración: " + tokenValue)
                    .build();
        }
    }

    @Override
    @Transactional
    public MessageResponse setPassword(SetPasswordRequest request) {
        // Buscar token
        Token token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Validar token
        if (token.isUsed()) {
            throw new RuntimeException("Este token ya ha sido utilizado");
        }

        if (token.isExpired()) {
            throw new RuntimeException("El token ha expirado");
        }

        if (token.getType() != TokenType.REGISTRATION) {
            throw new RuntimeException("Token inválido para esta operación");
        }

        // Actualizar usuario
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        userRepository.save(user);

        // Marcar token como usado
        token.setUsed(true);
        tokenRepository.save(token);

        return MessageResponse.builder()
                .message("Contraseña configurada exitosamente. Ahora puedes iniciar sesión.")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Obtener usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el usuario esté activo
        if (!user.isActive()) {
            throw new RuntimeException("Usuario inactivo. Por favor, configure su contraseña primero.");
        }

        // Generar token JWT
        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    @Transactional
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        // Buscar usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ese correo electrónico"));

        // Verificar que el usuario esté activo
        if (!user.isActive()) {
            throw new RuntimeException("Usuario inactivo. Por favor, complete el registro primero.");
        }

        // Generar token
        String tokenValue = UUID.randomUUID().toString();
        Token token = Token.builder()
                .token(tokenValue)
                .type(TokenType.PASSWORD_RESET)
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        tokenRepository.save(token);

        // Enviar correo
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), tokenValue);
            return MessageResponse.builder()
                    .message("Se ha enviado un correo electrónico con instrucciones para restablecer tu contraseña.")
                    .build();
        } catch (Exception e) {
            // Si falla el envío del email, igual se generó el token
            return MessageResponse.builder()
                    .message("Token generado. NOTA: No se pudo enviar el correo. Token de reseteo: " + tokenValue)
                    .build();
        }
    }

    @Override
    @Transactional
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        // Buscar token
        Token token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Validar token
        if (token.isUsed()) {
            throw new RuntimeException("Este token ya ha sido utilizado");
        }

        if (token.isExpired()) {
            throw new RuntimeException("El token ha expirado");
        }

        if (token.getType() != TokenType.PASSWORD_RESET) {
            throw new RuntimeException("Token inválido para esta operación");
        }

        // Actualizar contraseña
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Marcar token como usado
        token.setUsed(true);
        tokenRepository.save(token);

        return MessageResponse.builder()
                .message("Contraseña restablecida exitosamente. Ahora puedes iniciar sesión con tu nueva contraseña.")
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return convertToUserResponse(user);
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
