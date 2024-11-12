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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String email = getEmailFromContext();

        if (email != null) {
            UserService service = getUserService();
            UserEntity user = service.findByEmail(email);

            if (user == null || user.getUserStatus() != UserStatus.VERIFIED) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: User not verified");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getEmailFromContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }
}