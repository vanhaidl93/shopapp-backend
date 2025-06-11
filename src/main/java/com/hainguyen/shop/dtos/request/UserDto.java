package com.hainguyen.shop.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String fullName;
    protected String phoneNumber;
    private String address;
    private Date dateOfBirth;

}
