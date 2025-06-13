package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.isActive = true " +
            "AND (:keyword IS NULL OR :keyword = '' OR u.fullName LIKE %:keyword% " +
            "OR u.address LIKE %:keyword% " +
            "OR u.phoneNumber LIKE %:keyword%) " +
            "AND u.role.name = 'ROLE_USER'")
    Page<User> getAllUsersPerPageIncludeSearchKeyword(String keyword, Pageable pageable);
}
