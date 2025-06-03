package com.hainguyen.shop.dtos.request;

import com.hainguyen.shop.anotations.FieldsValueMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class UserRegister {
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "(\\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}([^\\d]+|$)",
            message = "Mobile number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Password cannot be blank")
    private String retryPassword;

    private String address;
    private boolean isActive;
    private Date dateOfBirth;
    private int facebookAccountId;
    private int googleAccountId;

    @NotNull(message = "Role ID is required")
    private Long roleId;
}
