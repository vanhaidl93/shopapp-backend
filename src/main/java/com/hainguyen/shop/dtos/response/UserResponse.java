package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse extends BaseResponse{

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private boolean isActive;
    private Date dateOfBirth;
    private int facebookAccountId;
    private int googleAccountId;

    private Long roleId;
}
