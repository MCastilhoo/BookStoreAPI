package com.br.BookStoreAPI.config;

import com.br.BookStoreAPI.models.entities.UserEntity;
import com.br.BookStoreAPI.services.UserService;
import com.br.BookStoreAPI.utils.enums.UserStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
public class VerifiedUserFilter extends OncePerRequestFilter {
    @Autowired
    private ApplicationContext applicationContext;
    private UserService userService;

    private UserService getUserService() {
        if (userService == null) {
            userService = applicationContext.getBean(UserService.class);
        }
        return userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String email = getEmailFromContext();

       if (email != null) {
           UserService userService = getUserService();
           UserEntity userEntity = userService.findByEmail(email);

           if (userEntity == null || userEntity.getUserStatus() != UserStatus.VERIFIED){
               response.setStatus(HttpServletResponse.SC_FORBIDDEN);
               response.setContentType("application/json");
               response.getWriter().write("{\"error\": \"User not verified\", \"message\": \"Check your email to activate account.\"}");
               return;
           }
       }
       filterChain.doFilter(request, response);
    }
    private String getEmailFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                return ((Jwt) principal).getClaimAsString("sub");
            } else if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }
        }
        return null;
    }
}