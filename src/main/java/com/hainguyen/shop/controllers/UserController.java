package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.RefreshTokenDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.configs.security.JwtTokenUtil;
import com.hainguyen.shop.mapper.UserMapper;
import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.services.ITokenService;
import com.hainguyen.shop.services.IUserService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserLoginDto;
import com.hainguyen.shop.dtos.response.LoginResponse;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final ITokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> createUser(@Valid @RequestBody UserRegister userRegister) {

        userService.createUser(userRegister);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(Constants.STATUS_201,
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_201)));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDto userLoginDto,
                                               HttpServletRequest request) {

        String token = userService.login(userLoginDto.getPhoneNumber(), userLoginDto.getPassword(),
                userLoginDto.getRoleId());

        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserByToken(token);
        Token newToken = tokenService.addToken(user, token, isMobile(userAgent));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toLoginResponse(user,
                        localizationUtils.getLocalizedMessage(Constants.LOGIN_SUCCESS),newToken));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByRefreshToken(refreshTokenDto.getRefreshToken());

        Token newToken = tokenService.refreshToken(refreshTokenDto.getRefreshToken(),user,authentication);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userMapper.toLoginResponse(user,localizationUtils.getLocalizedMessage(Constants.MESSAGE_200),newToken));

    }


    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserByToken(
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        User existingUser = userService.getUserByToken(token);

        return ResponseEntity.ok()
                .body(userMapper.mapToUserResponse(existingUser, new UserResponse()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
                                        @Valid @RequestBody UserDto userDto,
                                        @RequestHeader("Authorization") String bearerToken) {
        boolean isUpdated = false;
        String token = bearerToken.substring(7);
        if (jwtTokenUtil.validateToken(token, userId)) { // only the owner of valid token could update
            isUpdated = userService.updateUser(userId, userDto);
        }

        return localizationUtils.getResponseChangeRecord(isUpdated, Constants.MESSAGE_417_UPDATE);
    }

    private boolean isMobile(String userAgent) {
        return userAgent.contains("mobile");
    }
}
