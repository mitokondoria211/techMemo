package com.example.techMemo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JwtService {


    @Value("${jwt.expiration}")
    private long expiration;

    private final JwtEncoder jwtEncoder;
    private final JwtConfig jwtConfig;

    /**
     * トークンレスポンス
     */
    public record JwtToken(String token, String refreshToken, Date expiresAt) {
    }


//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }

//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }

    public JwtToken generateToken(Authentication authentication) {
        return generateToken(
            authentication.getName(),
            authentication.getAuthorities().stream()
                          .map(GrantedAuthority::getAuthority)
                          .collect(Collectors.toList()));

    }

    /**
     * UserDetailsからトークンを生成
     */
    public JwtToken generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(),
                             userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList()));
    }

    public JwtToken generateToken(String username, Iterable<String> roles) {
        // 現在の時刻
        Instant now = Instant.now();

        // アクセストークンの有効期限
        Instant accessTokenExpiry = now.plus(jwtConfig.getExpiration(), ChronoUnit.MILLIS);

        // リフレッシュトークンの有効期限
        Instant refreshTokenExpiry = now.plus(jwtConfig.getRefreshExpiration(), ChronoUnit.MILLIS);

        // アクセストークンに含めるクレーム
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        // アクセストークンの生成
        String accessToken = createToken(username, now, accessTokenExpiry, claims);

        // リフレッシュトークンの生成（権限情報は含めない）
        String refreshToken = createToken(username, now, refreshTokenExpiry, new HashMap<>());

        return new JwtToken(accessToken, refreshToken, Date.from(accessTokenExpiry));
    }

    /**
     * トークンの生成
     */
    private String createToken(String subject, Instant issuedAt, Instant expiresAt, Map<String, Object> claims) {
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                                                         .subject(subject)
                                                         .issuedAt(issuedAt)
                                                         .expiresAt(expiresAt);

        // カスタムクレームの追加
        claims.forEach(claimsBuilder::claim);

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claimsBuilder.build())).getTokenValue();
    }

//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }

//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }

//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public String generateToken(
//            Map<String, Object> extraClaims,
//            UserDetails userDetails
//    ) {
//
//        return Jwts.builder()
//                   .claims(extraClaims)
//                   .subject(userDetails.getUsername())
//                   .issuedAt(new Date(System.currentTimeMillis()))
//                   .expiration(new Date(System.currentTimeMillis() + expiration))
//                   .signWith(getSignInKey())
//                   .compact();
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parser()
//                //秘密鍵をセット
//                .verifyWith(getSignInKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    private SecretKey getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }


}
