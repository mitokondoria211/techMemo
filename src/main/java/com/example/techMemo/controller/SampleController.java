package com.example.techMemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    // 認証済みユーザーなら誰でもアクセス可
    @GetMapping("/profile")
    public ResponseEntity<String> profile(Authentication authentication) {
        String username = authentication.getName();  // subクレームの値
        return ResponseEntity.ok("Hello, " + username);
    }

    // ROLE_ADMIN のみアクセス可
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Admin page");
    }
}
