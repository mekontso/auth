package com.mas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mas.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        var user = authService.register(registerRequest.firstName, registerRequest.lastName, registerRequest.email, registerRequest.password, registerRequest.passwordConfirm);
        return new RegisterResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        var user = authService.login(loginRequest.email, loginRequest.password);
        return new LoginResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    record RegisterRequest(@JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, String email, String password, @JsonProperty("password_confirm") String passwordConfirm) { }

    record RegisterResponse(Long id, @JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, String email) { }

    record LoginRequest( String email, String password) { }

    record LoginResponse(Long id, @JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, String email) { }
}
