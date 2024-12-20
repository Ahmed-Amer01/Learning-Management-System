package com.example.lms.token;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blacklistedTokens")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String token;
}
