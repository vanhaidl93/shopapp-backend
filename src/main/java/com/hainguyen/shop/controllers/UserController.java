package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.RefreshTokenDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.utils.JwtTokenUtil;
import com.hainguyen.shop.dtos.response.UsersResponsePage;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

        String token = userService.login(userLoginDto);

        // add token into database.
        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserByToken(token);
        Token newToken = tokenService.addToken(user, token, isMobile(userAgent));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toLoginResponse(user,
                        localizationUtils.getLocalizedMessage(Constants.LOGIN_SUCCESS), newToken));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Token newToken = tokenService.refreshToken(refreshTokenDto.getRefreshToken(), authentication);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userMapper.toLoginResponse(newToken.getUser(),
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_200), newToken));
    }


    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<UserResponse> getUserByToken(
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        User existingUser = userService.getUserByToken(token);

        return ResponseEntity.ok()
                .body(userMapper.mapToUserResponse(existingUser, new UserResponse()));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<SuccessResponse> updateUser(@PathVariable Long userId,
                                                      @Valid @RequestBody UserDto userDto,
                                                      @RequestHeader("Authorization") String bearerToken) {
        boolean isUpdated = false;
        String token = bearerToken.substring(7);
        if(jwtTokenUtil.validateTokenOwner(token,userId)){
            // only the valid token owner could update
            isUpdated = userService.updateUser(userId, userDto);
        }

        return localizationUtils.getResponseChangeRecord(isUpdated, Constants.MESSAGE_417_UPDATE);
    }


    // ADMIN - get all users per page include search keyword
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsersPerPageIncludeSearchKeyword(@RequestParam(defaultValue = "1") int pageNumber,
                                                                    @RequestParam(defaultValue = "10") int pageSize,
                                                                    @RequestParam(defaultValue = "", required = false) String keyword) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").ascending());

        UsersResponsePage usersResponsePage = userService.getAllUsersPerPageIncludeSearchKeyword(pageable, keyword);

        return ResponseEntity.ok().body(usersResponsePage);

    }

    // ADMIN - reset any password for specified USER.
    @PutMapping("resetPassword/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> resetPassword(@PathVariable Long userId) {
        String newPassword = UUID.randomUUID().toString().substring(0, 10);
        userService.resetPassword(userId, "test@12345");

        return ResponseEntity.ok().body(newPassword);
    }

    // ADMIN - Block and enable a USER.
    @PutMapping("blockOrEnableUser/{userId}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> blockOrEnable(@PathVariable Long userId, @PathVariable int active) {

        userService.blockOrEnableUser(userId, active > 0);
        String message = active > 0 ? "Successfully enabled the user." : "Successfully blocked the user.";

        return ResponseEntity.ok().body(message);
    }

    private boolean isMobile(String userAgent) {
        return userAgent.contains("mobile");
    }
}
