package com.hainguyen.shop.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private String message;
}
