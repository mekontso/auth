package com.mas.service;

import com.mas.data.User;
import com.mas.data.UserRepo;
import com.mas.error.InvalidCredentialsError;
import com.mas.error.PasswordDoNotMatchError;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm) {
        if (!Objects.equals(password, passwordConfirm))
            throw new PasswordDoNotMatchError();
        return userRepo.save(
                User.of(firstName, lastName, email, passwordEncoder.encode(password))
        );
    }

    public User login(String email, String password) {
        var user = userRepo.findByEmail(email).orElseThrow(InvalidCredentialsError::new);
        if (!passwordEncoder.matches(password, user.getPassword())) throw new InvalidCredentialsError();
        return user;
    }
}
