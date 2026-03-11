package com.example.techMemo.url;

import java.time.LocalDateTime;

public record UrlResponse(
    Long id,
    String url,
    String title,

    Long articleId,
    String articleTitle,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
