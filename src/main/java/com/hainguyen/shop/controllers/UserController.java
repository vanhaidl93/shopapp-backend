package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.configs.security.JwtTokenUtil;
import com.hainguyen.shop.services.IUserService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserLoginDto;
import com.hainguyen.shop.dtos.response.LoginResponse;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> createUser(@Valid @RequestBody UserRegister userRegister) {

        userService.createUser(userRegister);

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(Constants.STATUS_201,
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_201)));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        String token = userService.login(userLoginDto.getPhoneNumber(), userLoginDto.getPassword(),
                                                        userLoginDto.getRoleId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponse(token,
                        localizationUtils.getLocalizedMessage(Constants.LOGIN_SUCCESS)));
    }


    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserByToken(
                                                @RequestHeader("Authorization") String bearerToken){
        String token = bearerToken.substring(7);
        UserResponse existingUser = userService.getUserByToken(token);

        return ResponseEntity.ok()
                .body(existingUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
                                       @Valid @RequestBody UserDto userDto,
                                        @RequestHeader("Authorization") String bearerToken) {
        boolean isUpdated = false;
        String token = bearerToken.substring(7);
        if(jwtTokenUtil.validateToken(token,userId)){ // only the owner of valid token could update
            isUpdated = userService.updateUser(userId, userDto);
        }

        return localizationUtils.getResponseChangeRecord(isUpdated,Constants.MESSAGE_417_UPDATE);
    }
}
