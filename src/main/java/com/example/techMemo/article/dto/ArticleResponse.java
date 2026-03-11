package com.example.techMemo.article.dto;

import com.example.techMemo.category.CategoryResponse;
import com.example.techMemo.tag.TagResponse;
import com.example.techMemo.user.UserResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArticleResponse(
    Long id,
    String title,
    String content,
    boolean publicFlag,
    UserResponse user,
    CategoryResponse category,
    List<TagResponse> tags,
//    List<UrlResponse> urls,
    long likeCount,
    boolean likedByMe,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
    ) {
}
