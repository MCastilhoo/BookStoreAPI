package com.br.BookStoreAPI.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "USER_VERIFIER")
public class UserVerifierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verifierId;

    @Column(name= "UUID", nullable = false)
    private UUID uuid;

    @Column(name = "EXPIRATION", nullable = false)
    private Instant expriation;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", unique = true)
    private UserEntity user;
}
