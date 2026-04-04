package com.example.techMemo.controller;

import com.example.techMemo.user.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public")
public class PublicController {


    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @PostMapping("/signup")
    public void createUser(@RequestBody User user) {

    }
}
