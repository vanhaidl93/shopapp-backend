package com.hainguyen.shop.filters;

import com.hainguyen.shop.utils.JwtTokenUtils;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.UserRepo;
import com.hainguyen.shop.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepo userRepo;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // extracting JWT on request
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            // extract token from request header "Authorization".
            final String authHeader = request.getHeader(Constants.JWT_HEADER);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            // extract phoneNumber and validate token follow signature - secret.
            String token = authHeader.substring(7);
            String subject = jwtTokenUtil.extractSubject(token);

            User existingUser = userRepo.findByPhoneNumberOrEmail(subject,subject)
                    .orElseThrow(() -> new ResourceNotFoundException("User","phoneNumber Or Email",subject));

            // save SecurityContext if first filter, to retrieve later.
            if (jwtTokenUtil.validateToken(token,existingUser)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                UsernamePasswordAuthenticationToken authenticationToken
                        = UsernamePasswordAuthenticationToken.authenticated(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }


    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                // excepting Order "GET"
                Pair.of(String.format("%s/roles**", apiPrefix), "GET"),
                Pair.of(String.format("%s/products**", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories**", apiPrefix), "GET"),

                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/logout", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/refreshToken", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/auth/socialLogin**", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/auth/social/callback**", apiPrefix), "GET"),

                Pair.of(String.format("%s/comments**", apiPrefix), "GET"),
                Pair.of(String.format("%s/coupons**", apiPrefix), "GET"),

                Pair.of(String.format("%s/payments**", apiPrefix), "GET"),
                Pair.of(String.format("%s/payments**", apiPrefix), "POST"),

                // Swagger
                Pair.of("/v3/api-docs**", "GET"),
                Pair.of("/swagger-resources**", "GET"),
                Pair.of("/swagger-ui**", "GET")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> bypassToken : bypassTokens) {
            String pathBypass = bypassToken.getFirst();
            String methodBypass = bypassToken.getSecond();
            // Check if the request path and method satisfy the condition.
            if (requestPath.matches(pathBypass.replace("**", ".*"))
                    && requestMethod.equalsIgnoreCase(methodBypass)) {


                return true;
            }
        }
        return false;
    }


}
