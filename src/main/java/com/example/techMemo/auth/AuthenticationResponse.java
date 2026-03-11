package com.example.techMemo.auth;

import com.example.techMemo.user.UserResponse;

/**
 * 認証レスポンス
 *
 * @param accessToken アクセストークン
 * @param refreshToken リフレッシュトークン
 */
public record AuthenticationResponse(String accessToken, String refreshToken, UserResponse user) {
}
