package com.hainguyen.shop.configs.security;

import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final Environment env;

    public String generateToken(User user) {

        return Jwts.builder()
                .claim("phoneNumber", user.getPhoneNumber())
                .claim("userId", user.getId().toString())
                .subject("JWT-TOKEN")
                .expiration(new Date(new Date().getTime() + 2_592_000_000L)) // milliseconds
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        // CAN BE OVERWRITE BY VARIABLE ENVIRONMENT
        String secret = env.getProperty(Constants.JWT_SECRET_KEY, Constants.JWT_SECRET_DEFAULT_VALUE);

        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseClaimsToken(String token) { // Claims  # Map<String,Object>
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

    public boolean validateToken(String token, Long UserId) {
        Long userIdExtract = extractUserId(token);
        if(!userIdExtract.equals(UserId) || isTokenExpired(token)) {
            throw new BadCredentialsException("Invalid token");
        }

        return true;
    }






}
