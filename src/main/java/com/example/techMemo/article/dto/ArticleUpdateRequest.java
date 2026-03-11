package com.example.techMemo.article.dto;

import com.example.techMemo.url.UrlRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ArticleUpdateRequest(
    @NotBlank
    @Size(min = 4, max = 255)
    String title,
    @NotBlank
    @Size(min = 4)
    String content,
    boolean publicFlag,
    Long categoryId,
    @Size(max = 5, message = "Urlは5個までです")
    List<UrlRequest> urls,
    @Size(max = 5, message = "タグは5個までです")
    List<String> tagNames
) {
}
