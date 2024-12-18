package com.example.lms.auth;

import com.example.lms.user.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
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
    
    @DeleteMapping("/logout")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> logout() {
        return authService.logout();
    }
    
    @GetMapping("/profile")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        return authService.getProfile(request);
    }
    
    @PutMapping("/profile")
    @RolesAllowed({"STUDENT", "ADMIN", "INSTRUCTOR"})
    public ResponseEntity<?> updateProfile(@RequestBody User user) {
        return authService.updateProfile(user);
    }
}
