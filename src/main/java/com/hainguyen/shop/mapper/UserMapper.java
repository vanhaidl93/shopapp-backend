package com.hainguyen.shop.mapper;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.dtos.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

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
}
