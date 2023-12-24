package com.shopping.online.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopping.online.models.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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

    @JsonProperty("password")
    private String password;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("address")
    private String address;

    @JsonProperty("gender")
    private boolean gender;

    @JsonProperty("status")
    private boolean status;

    public static UserResponse formUser(UserEntity userEntity){
        return UserResponse.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .dob(userEntity.getDob())
                .password(userEntity.getPassword())
                .avatar(userEntity.getAvatar())
                .address(userEntity.getAddress())
                .gender(userEntity.isGender())
                .status(userEntity.isStatus())
                .build();
    }
}
