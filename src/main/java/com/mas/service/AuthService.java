package com.mas.service;

import com.mas.data.User;
import com.mas.data.UserRepo;
import com.mas.error.EmailAlreadyExistsError;
import com.mas.error.InvalidCredentialsError;
import com.mas.error.PasswordDoNotMatchError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;
     private final String accessTokenSecret;
     private final String refreshTokenSecret;

    public AuthService(UserRepo userRepo,
                       PasswordEncoder passwordEncoder,
                       @Value("${application.security.access-token-secret}") String accessTokenSecret,
                       @Value("${application.security.refresh-token-secret}")String refreshTokenSecret) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm) {
        if (!Objects.equals(password, passwordConfirm))
            throw new PasswordDoNotMatchError();
        User user;
        try {
            user = userRepo.save(User.of(firstName, lastName, email, passwordEncoder.encode(password)));
        } catch (Exception exception) {
            throw new EmailAlreadyExistsError();
        }
        return user;
    }

    public Login login(String email, String password) {
        var user = userRepo.findByEmail(email).orElseThrow(InvalidCredentialsError::new);
        if (!passwordEncoder.matches(password, user.getPassword())) throw new InvalidCredentialsError();
        return Login.of(user.getId(), accessTokenSecret,refreshTokenSecret);
    }
}
