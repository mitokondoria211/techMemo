package com.example.techMemo.auth.token;

import com.example.techMemo.config.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * トークンリフレッシュコントローラー
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class TokenRefreshController {

    /**
     * JWTサービス
     */
    private final JwtService jwtService;

    /**
     * JWTデコーダー
     */
    private final JwtDecoder jwtDecoder;

    /**
     * ユーザー詳細サービス
     */
    private final UserDetailsService userDetailsService;

    /**
     * トークンリフレッシュレスポンス
     *
     * @param accessToken  アクセストークン
     * @param refreshToken リフレッシュトークン
     */
    public record TokenRefreshResponse(String accessToken, String refreshToken) {
    }

    /**
     * トークンリフレッシュ
     *
     * @param request リクエスト
     * @return トークンリフレッシュレスポンス
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
        HttpServletRequest request) {
        // Authorizationヘッダーからリフレッシュトークンを取得

        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return ResponseEntity.status(401).build();
            }
            String refreshToken = Arrays.stream(request.getCookies())
                                        .filter(cookie -> "refreshToken".equals(cookie.getName()))
                                        .findFirst().map(Cookie::getValue).orElse(null);

            if (refreshToken == null) {
                return ResponseEntity.status(401).build();
            }

            var jwt = jwtDecoder.decode(refreshToken);

            String username = jwt.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 新しいトークンの生成
            JwtService.JwtToken jwtToken = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                new TokenRefreshResponse(jwtToken.token(), refreshToken));
        } catch (JwtException | BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
