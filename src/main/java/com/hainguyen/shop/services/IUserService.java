package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.models.User;

public interface IUserService {
    void createUser(UserRegister userRegister);
    String login(String phoneNumber, String password, Long roleId);
    User getUserByToken(String token);

    boolean updateUser(Long userId, UserDto userDto);

    User getUserByRefreshToken(String refreshToken);
}
