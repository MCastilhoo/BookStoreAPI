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
@Table(name = "ADMINISTRATORS")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ADMIN_ID")
    private UUID adminId;


    @NotBlank
    @Column(name = "ADMINISTRATOR_NAME")
    private String administratorName;

    @NotBlank
    @Column(name = "ADMINISTRATOR_USERNAME")
    private String administratorUsername;

    @NotBlank
    @Column(name = "ADMINISTRATOR_PASSWORD")
    private String administratorPassword;

    @Column
    private LocalDateTime creationDate;

    @Column
    private LocalDateTime modificationDate;


    @PrePersist
    protected void onCreate() {creationDate = LocalDateTime.now();}

    @PreUpdate
    protected void onUpdate(){ modificationDate = LocalDateTime.now();}
}
