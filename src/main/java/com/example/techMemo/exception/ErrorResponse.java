package com.example.techMemo.exception;

public record ErrorResponse(
    int status,
    String message
) {
}
