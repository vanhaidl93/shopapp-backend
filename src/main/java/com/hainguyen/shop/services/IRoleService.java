package com.hainguyen.shop.services;

import com.hainguyen.shop.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRoleService {

    List<Role> getAllRoles();

}
