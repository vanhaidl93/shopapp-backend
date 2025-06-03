package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UserResponse;

public interface IUserService {
    void createUser(UserRegister userRegister);
    String login(String phoneNumber, String password, Long roleId);
    UserResponse getUserByToken(String token);

    boolean updateUser(Long userId, UserDto userDto);
}
