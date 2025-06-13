package com.hainguyen.shop.exceptions.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // populate dynamic values
        LocalDateTime currentTimeStamp = LocalDateTime.now();
        String message = (accessDeniedException!=null && accessDeniedException.getMessage() !=null) ?
                accessDeniedException.getMessage(): "Authorized failed";
        String path = request.getRequestURI();
        // construct response header and response status
        response.setHeader("shopapp-denied-reason","Authentication failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        // construct the JSON response
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse=
                String.format("{\"timestamp\":\"%s\"," +
                                "\"status\":%d," +
                                "\"error\":\"%s\"," +
                                "\"message\":\"%s\"," +
                                "\"path\":\"%s\"}",
                        currentTimeStamp, HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN.getReasonPhrase(),
                        message,path);
        response.getWriter().write(jsonResponse);
    }
}
