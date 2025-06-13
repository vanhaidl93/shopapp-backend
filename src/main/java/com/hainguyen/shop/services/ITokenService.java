package com.hainguyen.shop.services;

import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import org.springframework.security.core.Authentication;

public interface ITokenService {
    Token addToken(User user,String token, boolean isMobile);
    Token refreshToken(String refreshToken, Authentication authentication);

}
