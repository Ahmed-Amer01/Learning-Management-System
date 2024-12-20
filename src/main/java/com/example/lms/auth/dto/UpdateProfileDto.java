package com.example.lms.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileDto {
    @Size(max = 50, message = "Name must not exceed 50 characters")
    @NotEmpty(message = "Name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;
}
