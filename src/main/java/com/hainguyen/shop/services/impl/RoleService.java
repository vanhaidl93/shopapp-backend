package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.models.Role;
import com.hainguyen.shop.repositories.RoleRepo;
import com.hainguyen.shop.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepo roleRepo;

    @Override
    public List<Role> getAllRoles() {

        return roleRepo.findAll();
    }
}
