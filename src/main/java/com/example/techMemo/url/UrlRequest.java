package com.example.techMemo.url;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UrlRequest(
    @NotBlank
    String title,
    @NotBlank
    @URL
    String url,
    int sortOrder
) {
}
