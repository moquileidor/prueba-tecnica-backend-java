package org.jorge.pruebatecnica.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jorge.pruebatecnica.enums.TokenType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;
    
    @Builder.Default
    @Column(nullable = false)
    private boolean used = false;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !used && !isExpired();
    }
}
