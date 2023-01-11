package com.mas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mas.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final Integer refreshTokenValidity;

    public AuthController(AuthService authService, @Value("${application.security.refresh-token-validity}") Integer refreshTokenValidity) {
        this.authService = authService;
        this.refreshTokenValidity = refreshTokenValidity;
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
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        var login = authService.login(loginRequest.email, loginRequest.password);

        Cookie cookie = new Cookie("refresh_token", login.getRefreshToken().getToken());
        cookie.setMaxAge(refreshTokenValidity);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");

        response.addCookie(cookie);
        return new LoginResponse(login.getAccessToken().getToken());
    }

    record RegisterRequest(@JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, String email, String password, @JsonProperty("password_confirm") String passwordConfirm) { }

    record RegisterResponse(Long id, @JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName, String email) { }

    record LoginRequest( String email, String password) { }

    record LoginResponse( String token) { }
}
