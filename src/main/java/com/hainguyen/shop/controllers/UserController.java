package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserLoginDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.LoginResponse;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.dtos.response.UsersResponsePage;
import com.hainguyen.shop.mapper.UserMapper;
import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.services.ITokenService;
import com.hainguyen.shop.services.IUserService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.utils.JwtTokenUtils;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtils jwtTokenUtil;
    private final UserMapper userMapper;
    private final ITokenService tokenService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> createUser(@Valid @RequestBody UserRegister userRegister) {

        userService.createUser(userRegister);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(Constants.STATUS_201,
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_201)));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDto userLoginDto,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        String token = userService.login(userLoginDto);

        // add token (accessToken - refreshToken) into database.
        String userAgent = request.getHeader("User-Agent");
        User user = userService.getUserByToken(token);
        Token savedToken = tokenService.addToken(user, token, isMobile(userAgent));

        // Set refresh token as HttpOnly cookie (refresh token functionality)
        long cookieMaxAge = userLoginDto.isRememberMe() ? Duration.ofDays(7).getSeconds() : -1;
        // -1, the cookie is removed when the browser is closed
        // 0, the cookie should expire immediately
        ResponseCookie cookie = ResponseCookie.from("refreshToken", savedToken.getRefreshToken())
                .httpOnly(true)
                .secure(false) // https
//                .path("/api/v1/users") //error when running, success when debugging ??
                .path("/api/v1")
                .maxAge(cookieMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toLoginResponse(user,
                        localizationUtils.getLocalizedMessage(Constants.LOGIN_SUCCESS), savedToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(HttpServletResponse response) {
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1")
//                .path("/api/v1/users")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
        return ResponseEntity.ok()
                .body(new SuccessResponse(Constants.STATUS_200,
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_200)));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        Token newToken = tokenService.refreshToken(refreshToken);

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
        if (jwtTokenUtil.validateTokenOwner(token, userId)) {
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
