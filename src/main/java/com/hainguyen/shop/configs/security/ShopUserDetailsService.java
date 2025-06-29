package com.hainguyen.shop.configs.security;

import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User existingUser = userRepo.findByPhoneNumberOrEmail(username,username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Details not found for the user: " + username));

        GrantedAuthority authority= new SimpleGrantedAuthority(existingUser.getRole().getName());
        String principal = existingUser.getPhoneNumber() != null ? existingUser.getPhoneNumber() : existingUser.getEmail();

        return new org.springframework.security.core.userdetails.User(
                principal,existingUser.getPassword(), List.of(authority));
    }


}
