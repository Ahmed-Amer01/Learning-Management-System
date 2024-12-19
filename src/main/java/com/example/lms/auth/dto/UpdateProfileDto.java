package com.example.lms.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileDto {
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
