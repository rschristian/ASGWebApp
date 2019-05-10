package com.assessment.asg.services;

import com.assessment.asg.db.UserRepository;
import com.assessment.asg.models.AuthenticationToken;
import com.assessment.asg.models.PasswordResetDto;
import com.assessment.asg.models.User;
import com.assessment.asg.models.registration.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public interface UserService {

    User emailInUse(String email);

    void createAuthenticationToken(User user, String purpose);

    String authenticateUser(String token, String purpose);

    void changePassword(PasswordResetDto passwordResetDto);

    User registerNewUser(UserRegistrationDto accountDto);
}

@Service
class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private EmailService emailService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public User emailInUse(final String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void changePassword(final PasswordResetDto passwordResetDto) {
        passwordResetDto.setPassword(passwordEncoder().encode(passwordResetDto.getPassword()));
        userRepository.changePassword(passwordResetDto);
    }

    @Override
    public void createAuthenticationToken(final User user, final String purpose) {
        //How long, from now, until the token is expired
        int tokenExpiryTimeInMinutes = 24 * 60;

        String token = UUID.randomUUID().toString();
        String hashedToken = encodeToken(token);
        userRepository.createAuthenticationToken(new AuthenticationToken(user.getEmailAddress(), hashedToken,
                LocalDateTime.now().plusMinutes(tokenExpiryTimeInMinutes)));

        if (purpose.equalsIgnoreCase("register")) {
            String recipientAddress = user.getEmailAddress();
            String subject = "Registration Confirmation";
            String link = "https://localhost:8443/registrationConfirm?token=" + token;

            emailService.sendHTMLMessage(recipientAddress, subject, link);
        } else if (purpose.equalsIgnoreCase("password reset")) {

            String recipientAddress = user.getEmailAddress();
            String subject = "Password Reset";
            String link = "https://localhost:8443/passwordReset?token=" + token;
            emailService.sendHTMLMessage(recipientAddress, subject, link);
        }
    }

    @Override
    public String authenticateUser(final String token, final String purpose) {

        AuthenticationToken authenticationToken = userRepository.getAuthenticationToken(encodeToken(token));
        String invalidToken = "Sorry, but that token seems to be invalid. Make " +
                "sure it's correct, or sign up for an account with us if you don't already have one.";
        String expiredToken = "Sorry, but that token has expired. Please click " +
                "below to request a new one. All tokens do expire 24 hours after they are sent out.";
        if (purpose.equalsIgnoreCase("register")) {
            if (authenticationToken == null) {
                return invalidToken;
            }

            if (LocalDateTime.now().isAfter(authenticationToken.getExpiryDate())) {
                return expiredToken;
            }

            userRepository.authenticateUser(authenticationToken.getUserEmail());
        } else if (purpose.equalsIgnoreCase("password reset")) {
            if (authenticationToken == null) {
                return invalidToken;
            }

            if (LocalDateTime.now().isAfter(authenticationToken.getExpiryDate())) {
                return expiredToken;
            }

            User user = emailInUse(authenticationToken.getUserEmail());
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.singletonList(
                    new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return null;
    }

    @Override
    public User registerNewUser(final UserRegistrationDto registrationDto) {
        registrationDto.setPassword(passwordEncoder().encode(registrationDto.getPassword()));

        Integer insertResponse = userRepository.saveUser(registrationDto);

        if (insertResponse == 1) {
            return new User(registrationDto.getEmailAddress(), registrationDto.getPassword(), "candidate", false, true);
        } else {
            return null;
        }
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private String encodeToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(token.getBytes());
            return new String(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.info("No such algorithm while hashing authentication token");
            return token;
        }
    }
}
