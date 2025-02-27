package com.br.BookStoreAPI.services;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;




@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public CompletableFuture<String> sendEmail(String recipient, String subject, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(recipient);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);

            logger.info("Email enviado com sucesso para: {}", recipient);
            return CompletableFuture.completedFuture("Email enviado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", recipient, e.getMessage());
            return CompletableFuture.completedFuture("Erro ao enviar o email: " + e.getMessage());
        }
    }
}