package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.utils.JwtTokenUtils;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.TokenRepository;
import com.hainguyen.shop.services.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private static final int MAX_TOKENS = 3;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.expiration-refresh-token}")
    private long refreshJwtExpiration;

    @Override
    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    @Transactional
    public Token addToken(User user, String token, boolean isMobile) {
        List<Token> tokens = tokenRepository.findByUser(user);
        // tokens exceed MAX_TOKENS
        if (tokens.size() >= MAX_TOKENS) {
            boolean allTokensOfMobile = tokens.stream().allMatch(Token::isMobile);
            Token deletedToken;
            if (allTokensOfMobile) {
                deletedToken = tokens.get(0);
            } else {
                deletedToken = tokens.stream()
                        .filter(t -> !t.isMobile())
                        .findFirst()
                        .orElse(tokens.get(0));
            }
            tokenRepository.delete(deletedToken);
        }
        // convert string token to Token object - save database.
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .isMobile(isMobile)
                .tokenType("Bearer")
                .expirationDate(LocalDateTime.now().plusSeconds(jwtExpiration))
                .refreshToken(UUID.randomUUID().toString())
                .refreshExpirationDate(LocalDateTime.now().plusSeconds(refreshJwtExpiration))
                .build();

        return tokenRepository.save(newToken);
    }

    @Override
    @Transactional
    public Token refreshToken(String refreshToken) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);

        if (existingToken == null) {
            throw new ResourceNotFoundException("Token", "refreshToken", "xxx-xxx-xxx");
        }

        if (existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(existingToken);
            throw new IllegalArgumentException("Refresh token is expired");
        }

        String newToken = jwtTokenUtil.generateToken(existingToken.getUser());
        existingToken.setExpirationDate(LocalDateTime.now().plusSeconds(jwtExpiration));
        existingToken.setToken(newToken);

        return existingToken;
    }


}
