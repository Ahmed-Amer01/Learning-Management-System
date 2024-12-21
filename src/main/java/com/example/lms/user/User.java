package com.example.lms.user;

import com.example.lms.common.enums.UserRole;
import com.example.lms.course.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    // For Instructor Role: List of Courses they created
    @JsonIgnore // to break infinity loop (bidirectional relationships)
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;

    // For Student Role: List of Enrolled Courses (Many-to-Many with Course)
    @JsonIgnore
    @ManyToMany(mappedBy = "students", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> enrollments;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public String getUsername() {
        return id;
    }
}
