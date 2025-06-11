package com.hainguyen.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private String tokenType;
    private LocalDateTime expirationDate;
    private boolean revoked;
    private boolean expired;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean isMobile;






}
