package com.br.BookStoreAPI.config;


import com.br.BookStoreAPI.models.entities.RoleEntity;
import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.repositories.RoleRepository;
import com.br.BookStoreAPI.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (RoleEntity.RoleType value : RoleEntity.RoleType.values()) {
            RoleEntity role = new RoleEntity();
            role.setRole(value);
            role.setRoleId(value.getRoleId());
            roleRepository.save(role);
        }
        var roleAdmin = roleRepository.findByRoleName(RoleEntity.RoleType.ADMIN.name());
        var userAdmin = userRepository.findByUserEmail("admin@admin.com");

        userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin já existe");
                },
                () ->{
                    var user = new UserEntity();
                    user.setUserEmail("admin@admin.com");
                    user.setUserPassword(passwordEncoder.encode("admin"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
