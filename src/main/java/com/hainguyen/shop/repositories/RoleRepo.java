package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    boolean deleteRoleById(Long id);


}
