package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.Token;
import com.hainguyen.shop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    List<Token> findByUser(User user);

    Token findByToken(String token);

    Token findByRefreshToken(String refreshToken);
}
