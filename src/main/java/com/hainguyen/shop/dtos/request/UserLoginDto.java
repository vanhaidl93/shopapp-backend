package com.hainguyen.shop.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UserLoginDto {

    @Pattern(regexp = "(\\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}([^\\d]+|$)",
            message = "Mobile number must be 10 digits")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Invalid email format")
    private String email;

    private String password;

    private Long roleId;
    private boolean rememberMe;

    // OAuth
    private String profileImage;
    private String fullName;
    private String googleAccountId;
    private String facebookAccountId;

    //
    public boolean isPasswordBlank() {
        return password == null || password.trim().isEmpty();
    }

    public boolean isFacebookAccountIdValid() {
        return facebookAccountId != null && !facebookAccountId.isEmpty();
    }

    public boolean isGoogleAccountIdValid() {
        return googleAccountId != null && !googleAccountId.isEmpty();
    }
}
