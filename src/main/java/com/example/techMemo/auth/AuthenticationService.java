package com.example.techMemo.auth;

import com.example.techMemo.config.JwtService;
import com.example.techMemo.mapper.UserMapper;
import com.example.techMemo.user.Role;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

//    /**
//     * 認証リクエスト
//     *
//     * @param email メールアドレス
//     * @param password パスワード
//     */
//    public record AuthenticationRequest(String email, String password) {
//    }
//
//    /**
//     * 登録リクエスト
//     *
//     * @param firstname 名前
//     * @param lastname 姓
//     * @param email メールアドレス
//     * @param password パスワード
//     */
//    public record RegisterRequest(String firstname, String lastname, String email, String password) {
//    }
//
//    /**
//     * 認証レスポンス
//     *
//     * @param accessToken アクセストークン
//     * @param refreshToken リフレッシュトークン
//     */
//    public record AuthenticationResponse(String accessToken, String refreshToken) {
//    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .name(request.name())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);


        return new AuthenticationResponse(jwtToken.token(), jwtToken.refreshToken(), userMapper.toUserResponse(user));
//        return AuthenticationResponse.builder()
//            .token(jwtToken)
//            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication =  authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = repository.findByEmail(request.email())
            .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        var jwtToken = jwtService.generateToken(authentication);
        return new AuthenticationResponse(jwtToken.token(), jwtToken.refreshToken(),userMapper.toUserResponse(user));

    }
}
