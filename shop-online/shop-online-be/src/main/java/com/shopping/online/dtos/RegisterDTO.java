package com.shopping.online.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopping.online.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @JsonProperty("first_name")
    @NotBlank(message = "First name is not blank")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name is not blank")
    private String lastName;

    @Email(message = "Email is not valid")
    private String email;

    @JsonProperty("phone_number")
    @Pattern(regexp = "[0-9]{10}", message = "Phone number it not valid")
    private String phoneNumber;

    @NotBlank(message = "New password is not blank")
    private String password;

    @NotBlank(message = "Repassword is not blank")
    private String repassword;

    private String avatar;

    @NotBlank(message = "Address is not blank")
    private String address;

    private boolean gender;

    private Date dob;

    @NotEmpty(message = "Role is not empty")
    private List<Long> roles;
}
