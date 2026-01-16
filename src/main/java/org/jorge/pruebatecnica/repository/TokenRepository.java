package org.jorge.pruebatecnica.repository;

import org.jorge.pruebatecnica.entity.Token;
import org.jorge.pruebatecnica.entity.User;
import org.jorge.pruebatecnica.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    Optional<Token> findByToken(String token);
    
    Optional<Token> findByUserAndTypeAndUsedFalse(User user, TokenType type);
}
