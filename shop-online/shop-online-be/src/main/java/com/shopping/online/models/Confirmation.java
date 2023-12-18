package com.shopping.online.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "confirmation")

public class Confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createDate;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    private UserEntity userEntity;


    public Confirmation(UserEntity userEntity, String token){
        this.userEntity = userEntity;
        this.createDate = LocalDateTime.now();
        this.token = token;
    }

}