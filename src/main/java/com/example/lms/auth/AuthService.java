package com.example.lms.auth;

import com.example.lms.auth.dto.LoginDto;
import com.example.lms.auth.dto.RegisterDto;
import com.example.lms.auth.dto.UpdateProfileDto;
import com.example.lms.token.BlacklistedToken;
import com.example.lms.token.TokenRepository;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenRepository tokenRepository;
    
    public ResponseEntity<?> register(RegisterDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());
        User savedUser = userRepository.save(newUser);
        String token = jwtService.generateToken(savedUser);
        Map<String, Object> response = new HashMap<>();
        user.setPassword(null); // Exclude password
        response.put("token", token);
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    public ResponseEntity<?> login(LoginDto user) {
        User foundUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (foundUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = jwtService.generateToken(foundUser);
        foundUser.setPassword(null); // Exclude password
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", foundUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public ResponseEntity<?> logout(String token) {
        BlacklistedToken storedBlacklistedToken = tokenRepository.findByToken(token).orElse(null);
        if (storedBlacklistedToken == null) {
            BlacklistedToken newBlacklistedToken = new BlacklistedToken();
            newBlacklistedToken.setToken(token);
            tokenRepository.save(newBlacklistedToken);
        }
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
    
    public ResponseEntity<?> getProfile(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        user.setPassword(null); // Exclude password
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    public ResponseEntity<?> updateProfile(String id, UpdateProfileDto user) {
        User foundUser = userRepository.findById(id).orElse(null);
        if (foundUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        foundUser.setName(user.getName());
        foundUser.setEmail(user.getEmail());
        foundUser = userRepository.save(foundUser);
        foundUser.setPassword(null); // Exclude password
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }
}
