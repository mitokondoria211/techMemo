package com.example.techMemo.auth;

/**
 * 登録リクエスト
 *
 * @param name ユーザー名
 * @param email メールアドレス
 * @param password パスワード
 */
public record RegisterRequest(String name, String email, String password) {
}
