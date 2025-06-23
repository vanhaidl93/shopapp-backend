package com.hainguyen.shop.utils;

import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final Environment env;
    @Value("${jwt.secretKey}")
    private String secret;
    @Value("${jwt.expiration}")
    private long EXPIRATION_TOKEN;
    private final TokenRepository tokenRepository;

    public String generateToken(Authentication
                                         authentication, User user) {

        return Jwts.builder()
                .claim("phoneNumber", authentication.getName())
                .claim("userId",user.getId().toString())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .subject("JWT-TOKEN")
                .expiration(new Date(new Date().getTime() + EXPIRATION_TOKEN*1000)) // milliseconds
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseClaimsToken(String token) {
        // Claims  # Map<String,Object>
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = this.parseClaimsToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public String extractPhoneNumber(String token) {
        return String.valueOf(this.parseClaimsToken(token).get("phoneNumber"));
    }

    public Long extractUserId(String token) {
        return Long.valueOf(this.parseClaimsToken(token).get("userId").toString());
    }

    public boolean validateToken(String token, User user) {
        Token existingToken = tokenRepository.findByToken(token);

        if( existingToken == null || existingToken.isRevoked()|| isTokenExpired(token) || !user.isActive()) {
            throw new BadCredentialsException("Unauthorized");
        }
        return true;
    }

    public boolean validateTokenOwner(String token, Long userId) {
        Long userIdExtract = extractUserId(token);
        if(!userIdExtract.equals(userId) || isTokenExpired(token)) {
            throw new BadCredentialsException("Unauthorized");
        }
        return true;
    }






}
