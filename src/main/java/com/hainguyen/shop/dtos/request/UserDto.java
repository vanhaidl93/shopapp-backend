package com.hainguyen.shop.dtos.request;

import com.hainguyen.shop.anotations.FieldsValueMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch ="retryPassword",
                message = "Passwords to not match!"
        )})
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String fullName;

    private String address;
    private Date dateOfBirth;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Password cannot be blank")
    private String retryPassword;

}
