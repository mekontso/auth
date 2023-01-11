package com.mas;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepo userRepo;

    public AuthController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello world";
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userRepo.save(user);
    }
}
