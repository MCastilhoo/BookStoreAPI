package com.br.BookStoreAPI.models.entities;

import com.br.BookStoreAPI.models.DTOs.loginDTOs.LoginRequestDTO;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userdId;

    @NotBlank
    @Column(name = "USER_FIRST_NAME")
    private String userFirstName;

    @NotBlank
    @Column(name = "USER_LAST_NAME")
    private String userLastName;

    @NotBlank
    @Column(name = "USER_EMAIL")
    private String userEmail;

    @NotBlank
    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleEntity> roles;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;


    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modificationDate = LocalDateTime.now();
    }

    public boolean getRoleByName(String roleName) {
        try {
            for (RoleEntity role : roles) {
                if (role.getRole() == RoleEntity.RoleType.valueOf(roleName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLoginCorrect(LoginRequestDTO loginRequestDTO, PasswordEncoder passwordEncoder) {
        passwordEncoder.matches(loginRequestDTO.password(), this.userPassword);
        return passwordEncoder.matches(loginRequestDTO.password(), this.userPassword);
    }
}