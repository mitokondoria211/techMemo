package com.example.techMemo.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
    @NotBlank(message = "カテゴリ名は必須です") @Size(min = 4, max = 30,message = "カテゴリ名は4〜30文字で入力してください") String name) {
}
