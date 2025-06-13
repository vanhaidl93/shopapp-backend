package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UsersResponsePage;
import com.hainguyen.shop.models.User;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    void createUser(UserRegister userRegister);

    String login(String phoneNumber, String password, Long roleId);

    User getUserByToken(String token);

    boolean updateUser(Long userId, UserDto userDto);

    UsersResponsePage getAllUsersPerPageIncludeSearchKeyword (Pageable pageable, String keyword );

    void resetPassword(Long userId, String newPassword);

    void blockOrEnableUser (Long userId, boolean active);

}
