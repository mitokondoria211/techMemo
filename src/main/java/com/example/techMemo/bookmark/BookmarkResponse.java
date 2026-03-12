package com.example.techMemo.bookmark;

import java.time.LocalDateTime;

public record BookmarkResponse(
    Long id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String url,
    String title,
    int sortOrder
) {
}
