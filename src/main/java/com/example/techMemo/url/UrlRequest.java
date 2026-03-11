package com.example.techMemo.url;

public record UrlRequest(
    String url,
    String title,

    int sortOrder) {
}
