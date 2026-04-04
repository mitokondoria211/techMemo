package com.example.techMemo.user;

public record UserDetailResponse(
    String id,
    String name,
    String email,
    Role role
) {
}
