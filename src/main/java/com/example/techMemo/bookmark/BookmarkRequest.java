package com.example.techMemo.bookmark;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record BookmarkRequest(
    @NotBlank(message = "ブックマークは必須です")
    @Size(min = 4, max = 30, message = "ブックマークは4〜30文字で入力してください")
    String title,
    @NotBlank @URL(message = "urlの形式ではありません")
    @Max(value = 100, message = "100文字以下で入力してください")
    String url,
    @Max(value = 255, message = "255文字以下で入力してください")
    String memo,
    int sortOrder
) {
}
