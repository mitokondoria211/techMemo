package com.example.techMemo.user;

public record UserResponse(
    String id,
    String name,
    String email,
    Role role) {
}
