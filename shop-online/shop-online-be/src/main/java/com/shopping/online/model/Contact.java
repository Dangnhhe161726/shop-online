package com.shopping.online.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email_contact")
    private String emailContact;

    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime contactDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "respond_date")
    private String responDate;

    @Column(name = "contact_content")
    private String contactContent;

    @Column(name = "respond_content")
    private String respondContent;

    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
