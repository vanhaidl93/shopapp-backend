package com.hainguyen.shop.configs.security;

import com.hainguyen.shop.exceptions.security.CustomAccessDeniedHandler;
import com.hainguyen.shop.exceptions.security.CustomAuthenticationEntryPoint;
import com.hainguyen.shop.models.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
public class ShopProjectSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement(scf -> scf
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(corsConfig -> corsConfig.configurationSource(
                        new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                                config.setAllowedMethods(Collections.singletonList("*"));
                                config.setAllowCredentials(true);
                                config.setAllowedHeaders(List.of("*"));
                                config.setMaxAge(3600L);
                                config.setExposedHeaders(List.of("Authorization"));
                                return config;
                            }
                        }
                )
        );

        http.exceptionHandling(ecf -> ecf
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.authorizeHttpRequests(req -> req
                .requestMatchers(
                        String.format("%s/users/register", apiPrefix),
                        String.format("%s/users/login", apiPrefix),
                        // show "/error" path that's controlled by spring security.
                        "/error",
                        // generate fake products
                        String.format("%s/products/generateFakeProducts", apiPrefix),
                        // swagger-UI
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                ).permitAll()

                .requestMatchers(GET, String.format("%s/users**", apiPrefix)).hasAnyRole(Role.USER)
                .requestMatchers(PUT, String.format("%s/users**", apiPrefix)).hasAnyRole(Role.USER)

                .requestMatchers(GET, String.format("%s/roles**", apiPrefix)).permitAll()

                .requestMatchers(GET, String.format("%s/categories**", apiPrefix)).permitAll()
                .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).permitAll()
                .requestMatchers(GET, String.format("%s/products/images/*", apiPrefix)).permitAll()
                .requestMatchers(POST, String.format("%s/products/generateFakeProducts", apiPrefix)).permitAll()
                .requestMatchers(POST, String.format("%s/products**", apiPrefix)).hasAnyRole(Role.ADMIN)
                .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                .requestMatchers(GET, String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
                .requestMatchers(GET, String.format("%s/orders/search-keyword", apiPrefix)).hasRole(Role.ADMIN)
                .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
                .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                .requestMatchers(GET, String.format("%s/orderDetails/**", apiPrefix)).hasAnyRole(Role.USER)
                .requestMatchers(POST, String.format("%s/orderDetails/**", apiPrefix)).hasAnyRole(Role.USER)
                .requestMatchers(PUT, String.format("%s/orderDetails/**", apiPrefix)).hasRole(Role.ADMIN)
                .requestMatchers(DELETE, String.format("%s/orderDetails/**", apiPrefix)).hasRole(Role.ADMIN)

                .anyRequest().authenticated()
        );



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker CompromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }


}
