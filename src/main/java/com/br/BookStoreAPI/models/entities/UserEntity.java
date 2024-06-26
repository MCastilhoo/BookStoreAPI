package com.br.BookStoreAPI.models.entities;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID")
    private UUID userdId;

    @NotBlank
    @Column(name = "USER_NAME")
    private String userName;

    @NotBlank
    @Column(name = "USER_EMAIL")
    private String userEmail;

    @NotBlank
    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;


    @PrePersist
    protected void onCreate() {creationDate = LocalDateTime.now();}

    @PreUpdate
    protected void onUpdate(){ modificationDate = LocalDateTime.now();}
}
