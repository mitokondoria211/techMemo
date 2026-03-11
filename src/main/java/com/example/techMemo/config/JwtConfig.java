package com.example.techMemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    // トークンの有効期限（ms）
    private long expiration;
    // リフレッシュトークンの有効期限（ms）
    private long refreshExpiration;
}
