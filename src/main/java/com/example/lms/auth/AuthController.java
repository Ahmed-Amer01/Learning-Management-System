package com.example.lms.auth;

import com.example.lms.user.User;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return authService.register(user);
    }
    
    
    @PostMapping( "/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return authService.login(user);
    }
    
    @GetMapping("/profile")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> profile() {
        return authService.profile();
    }
}
