package org.jorge.pruebatecnica.service;

import org.jorge.pruebatecnica.dto.request.*;
import org.jorge.pruebatecnica.dto.response.AuthResponse;
import org.jorge.pruebatecnica.dto.response.MessageResponse;
import org.jorge.pruebatecnica.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    
    MessageResponse register(RegisterRequest request);
    
    MessageResponse setPassword(SetPasswordRequest request);
    
    AuthResponse login(LoginRequest request);
    
    MessageResponse forgotPassword(ForgotPasswordRequest request);
    
    MessageResponse resetPassword(ResetPasswordRequest request);
    
    List<UserResponse> getAllUsers();
    
    UserResponse getUserById(Long id);
}
