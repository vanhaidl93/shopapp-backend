package com.hainguyen.shop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "users")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Builder
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fullname")
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;
    private String address;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // JSON --> JACKSON ONLY
    private String password;
    private boolean isActive;
    private Date dateOfBirth;
    private int facebookAccountId;
    private int googleAccountId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;



}
