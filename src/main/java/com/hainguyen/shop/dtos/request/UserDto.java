package com.hainguyen.shop.dtos.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String fullName;
    @Pattern(regexp = "(\\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}([^\\d]+|$)",
            message = "Mobile number must be 10 digits")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Invalid email format")
    private String email;
    private String address;
    private Date dateOfBirth;

}
