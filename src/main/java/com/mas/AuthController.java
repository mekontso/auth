package com.mas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mas.data.User;
import com.mas.data.UserRepo;
import com.mas.error.PasswordDoNotMatchError;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepo userRepo;

    public AuthController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        if(!Objects.equals(registerRequest.password(), registerRequest.passwordConfirm()))
            throw new PasswordDoNotMatchError();
        var user = userRepo.save(
                User.of(
                        registerRequest.firstName,
                        registerRequest.lastName,
                        registerRequest.email,
                        registerRequest.password
                )
        );
        return new RegisterResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    record RegisterRequest(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            String email,
            String password,
            @JsonProperty("password_confirm") String passwordConfirm
    ) {
    }

    record RegisterResponse(
            Long id,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            String email
    ) {
    }
}
