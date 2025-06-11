package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.configs.security.JwtTokenUtil;
import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.exceptions.UserAlreadyExistsException;
import com.hainguyen.shop.mapper.UserMapper;
import com.hainguyen.shop.models.Role;
import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.RoleRepo;
import com.hainguyen.shop.repositories.TokenRepository;
import com.hainguyen.shop.repositories.UserRepo;
import com.hainguyen.shop.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;


    @Override
    public void createUser(UserRegister userRegister) {
        String phoneNumber = userRegister.getPhoneNumber();
        if (userRepo.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException("User already register with given mobiNumber "
                    + phoneNumber);
        }
        Role role = roleRepo.findById(userRegister.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", userRegister.getRoleId().toString()));

        User newUser = User.builder()
                .fullName(userRegister.getFullName())
                .phoneNumber(userRegister.getPhoneNumber())
                .password(userRegister.getPassword())
                .address(userRegister.getAddress())
                .dateOfBirth(userRegister.getDateOfBirth())
                .facebookAccountId(userRegister.getFacebookAccountId())
                .googleAccountId(userRegister.getGoogleAccountId())
                .isActive(true)
                .role(role)
                .build();
        if (userRegister.getGoogleAccountId() == 0 && userRegister.getFacebookAccountId() == 0) {
            String password = userRegister.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        userRepo.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) {

        User existingUser = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BadCredentialsException("Invalid input"));

        if(!existingUser.isActive()){
            throw new BadCredentialsException("User is locked");
        }

        String jwt = "";
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(phoneNumber, password);
        Authentication authenticationRes = authenticationManager.authenticate(authenticationToken);

        if (authenticationRes != null && authenticationRes.isAuthenticated()) {
            jwt = jwtTokenUtil.generateToken(authenticationRes,existingUser);
        }

        return jwt;
    }

    @Override
    public User getUserByToken(String token) {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new BadCredentialsException("Unauthorized");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);

        return userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNumber", phoneNumber));
    }

    @Override
    @Transactional
    public boolean updateUser(Long userId, UserDto userDto) {

        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        User saveUpdatedUser = userRepo.save(userMapper.mapToUser(userDto, existingUser));

        return true;
    }

    @Override
    public User getUserByRefreshToken(String refreshToken) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if (existingToken == null){
            throw new ResourceNotFoundException("Token","refreshToken","xxx-xxx-xxx");
        }
        return existingToken.getUser();
    }
}
