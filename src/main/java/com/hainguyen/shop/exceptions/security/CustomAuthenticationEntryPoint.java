package com.hainguyen.shop.exceptions.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // populate dynamic values
        LocalDateTime currentTimeStamp = LocalDateTime.now();
        String message = (authException !=null && authException.getMessage() !=null) ?
                authException.getMessage(): "Unauthorized";
        String path = request.getRequestURI();
        // construct response header and response status
        response.setHeader("shopapp-error-reason","Authentication failed");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        // construct the JSON response
//        response.resetBuffer();
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse=
                String.format("{\"timestamp\":\"%s\"," +
                        "\"status\":%d," +
                        "\"error\":\"%s\"," +
                        "\"message\":\"%s\"," +
                        "\"path\":\"%s\"}",
                        currentTimeStamp, HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        message,path);
        response.getWriter().write(jsonResponse);

    }
}
