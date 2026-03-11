package com.example.techMemo.category;

import java.time.LocalDateTime;

public record CategoryResponse(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
