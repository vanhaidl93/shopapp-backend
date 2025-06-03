package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.dtos.request.UserDto;
import com.hainguyen.shop.dtos.request.UserRegister;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.exceptions.UserAlreadyExistsException;
import com.hainguyen.shop.mapper.UserMapper;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.configs.security.JwtTokenUtil;
import com.hainguyen.shop.models.Role;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.RoleRepo;
import com.hainguyen.shop.repositories.UserRepo;
import com.hainguyen.shop.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


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
                .orElseThrow(() -> new BadCredentialsException("Invalid phone number/password"));

        if(!existingUser.isActive()){
            throw new BadCredentialsException("User is locked");
        }

        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Invalid phone number/password");
            }
        }

        Optional<Role> optionalRole = roleRepo.findById(roleId);
        if(optionalRole.isEmpty() || roleId !=existingUser.getRole().getId()) {
            throw new BadCredentialsException("Invalid roleId");
        }

        // authenticate through Internal work flow of Spring Security
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(
                        phoneNumber, password,
                        List.of(new SimpleGrantedAuthority(existingUser.getRole().getName())));
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public UserResponse getUserByToken(String token) {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new BadCredentialsException("Invalid token");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        User existingUser = userRepo.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User","phoneNumber",phoneNumber));

        return userMapper.mapToUserResponse(existingUser,new UserResponse());
    }

    @Override
    @Transactional
    public boolean updateUser(Long userId, UserDto userDto) {

        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",userId.toString()));

        User saveUpdatedUser= userRepo.save(userMapper.mapToUser(userDto,existingUser));

        return true;
    }
}
