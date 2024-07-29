package com.br.BookStoreAPI.config;

import com.br.BookStoreAPI.models.entities.RoleEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;


@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var userAdmin = userService.getUserByEmail("admin@admin.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin user already exists. Skipping...\nProgram started.");
                },
                () -> {
                    var user = new UserEntity();
                    user.setUserFirstName("Admin");
                    user.setUserLastName("Admin");
                    user.setUserPassword(passwordEncoder.encode("admin"));
                    user.setRoles(Set.of(userService.getRoleByName(RoleEntity.RoleType.ADMIN.name())));
                }
        );
    }@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}