package com.utsav.arts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails related to user accounts.
 * Currently, supports sending account verification emails.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * The "from" email address configured in application properties.
     */
    @Value("${app.mail.from}")
    private String fromEmail;

    /**
     * Sends a verification email to a user during registration.
     * <p>
     * The email contains a 6-digit verification code that expires in 10 minutes.
     *
     * @param toEmail Recipient email address
     * @param code    Verification code to include in the email
     */
    public void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("OopsGallery: Account Verification Code");
        message.setText("""
        Welcome to OopsGallery 🎨
        
        Your verification code is: %s
        
        This code expires in 10 minutes.
        """.formatted(code));

        mailSender.send(message);
    }
}
