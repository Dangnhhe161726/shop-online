package com.shopping.online.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopping.online.models.Role;
import com.shopping.online.models.UserEntity;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("full_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("date_of_birth")
    private Date dob;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("address")
    private String address;

    @JsonProperty("gender")
    private boolean gender;

    @JsonProperty("status")
    private boolean status;

}
