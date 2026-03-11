package com.example.techMemo.auth;

/**
 * 認証リクエスト
 *
 * @param email メールアドレス
 * @param password パスワード
 */
public record AuthenticationRequest(String email, String password) {
}
