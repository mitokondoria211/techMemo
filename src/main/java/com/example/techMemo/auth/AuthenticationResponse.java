package com.example.techMemo.auth;

import com.example.techMemo.user.UserResponse;

/**
 * 認証レスポンス
 *
 * @param accessToken アクセストークン
 */
public record AuthenticationResponse(String accessToken, UserResponse user) {
}
