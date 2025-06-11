package com.hainguyen.shop.mapper;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.response.LoginResponse;
import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.dtos.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;


    public UserResponse mapToUserResponse(User user,
                                          UserResponse userResponse) {
        userResponse= modelMapper.map(user, UserResponse.class);
        userResponse.setId(user.getId());

        return userResponse;
    }

    public User mapToUser(UserDto userDto, User user) {
        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setId));
        modelMapper.map(userDto, user);

       return user;
    }

    public LoginResponse toLoginResponse(User user, String localizedMessage, Token token){
       return LoginResponse.builder()
                .message(localizedMessage)
                .token(token.getToken())
                .tokenType(token.getTokenType())
                .username(user.getPhoneNumber())
                .role(user.getRole())
                .id(user.getId())
                .build();
    }
}
