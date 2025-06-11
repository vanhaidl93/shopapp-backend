package com.hainguyen.shop.services;

import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;

public interface ITokenService {
    Token addToken(User user,String token, boolean isMobile);

}
