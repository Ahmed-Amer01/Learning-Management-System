package com.example.lms.auth;

import com.example.lms.auth.dto.LoginDto;
import com.example.lms.auth.dto.RegisterDto;
import com.example.lms.auth.dto.UpdateProfileDto;
import com.example.lms.user.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto user) {
        return authService.register(user);
    }
    
    
    @PostMapping( "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto user) {
        return authService.login(user);
    }
    
    @DeleteMapping("/logout")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);
        return authService.logout(token);
    }
    
    @GetMapping("/profile")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String id = jwtService.extractUsername(authHeader.substring(7));
        return authService.getProfile(id);
    }
    
    @PutMapping("/profile")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileDto user, HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String id = jwtService.extractUsername(authHeader.substring(7));
        return authService.updateProfile(id, user);
    }
}
