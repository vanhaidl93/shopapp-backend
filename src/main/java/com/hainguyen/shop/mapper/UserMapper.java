package com.hainguyen.shop.mapper;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserLoginDto;
import com.hainguyen.shop.dtos.response.LoginResponse;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.models.Role;
import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;


    public UserResponse mapToUserResponse(User user,
                                          UserResponse userResponse) {
        userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setId(user.getId());

        return userResponse;
    }

    public User mapToUser(UserDto userDto, User user) {
        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setId));
        modelMapper.map(userDto, user);

        return user;
    }

    public LoginResponse toLoginResponse(User user, String localizedMessage, Token token) {
        return LoginResponse.builder()
                .message(localizedMessage)
                .token(token.getToken())
                .refreshToken(token.getRefreshToken())
                .tokenType(token.getTokenType())
                .username(user.getPhoneNumber())
                .role(user.getRole())
                .id(user.getId())
                .build();
    }

    public User toUser(UserLoginDto userLoginDto, Role role){
       return User.builder()
                .fullName(Optional.ofNullable(userLoginDto.getFullName()).orElse(""))
                .email(Optional.ofNullable(userLoginDto.getEmail()).orElse(""))
                .profileImage(Optional.ofNullable(userLoginDto.getProfileImage()).orElse(""))
                .role(role)
                .googleAccountId(userLoginDto.getGoogleAccountId())
                .password("")
                .isActive(true)
                .build();
    }

    public UserLoginDto toUserLoginDto(String loginType,Map<String,Object> userInfo){
        // Extract userDtoLogin from userInfo map
        String accountId = "";
        String name = "";
        String picture = "";
        String email = "";

        switch (loginType.trim()) {
            case "google" -> {
                accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
                name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
                picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");
                email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
            }
            case "facebook" -> {
                accountId = (String) Objects.requireNonNullElse(userInfo.get("id"), "");
                name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
                email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
                // retrieve URL picture Facebook
                Object pictureObj = userInfo.get("picture");
                if (pictureObj instanceof Map<?, ?> pictureData) {
                    Object dataObj = pictureData.get("data");
                    if (dataObj instanceof Map<?, ?> dataMap) {
                        Object urlObj = dataMap.get("url");
                        if (urlObj instanceof String) {
                            picture = (String) urlObj;
                        }
                    }
                }
            }
            default -> {
                throw  new IllegalArgumentException("Unsupported login type: " + loginType);
            }
        }

        UserLoginDto userLoginDto = UserLoginDto.builder()
                .email(email)
                .fullName(name)
                .password("")
                .phoneNumber("")
                .rememberMe(true)
                .profileImage(picture)
                .build();

        if (loginType.trim().equals("google")) {
            userLoginDto.setGoogleAccountId(accountId);
        } else if (loginType.trim().equals("facebook")) {
            userLoginDto.setFacebookAccountId(accountId);
        }
        return  userLoginDto;
    }
}
