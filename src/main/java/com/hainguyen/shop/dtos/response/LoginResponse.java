package com.hainguyen.shop.dtos.response;

import com.hainguyen.shop.models.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginResponse {

    private String message;
    private String token;
    private String tokenType = "Bearer";

    //user's detail
    private Long id;
    private String username;
    private Role role;

}
