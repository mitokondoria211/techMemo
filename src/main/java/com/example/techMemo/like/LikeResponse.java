package com.example.techMemo.like;

import java.time.LocalDateTime;

public record LikeResponse(
    Long id,
    LocalDateTime createdAt
) {
}
