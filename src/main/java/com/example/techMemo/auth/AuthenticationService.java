package com.example.techMemo.auth;

import com.example.techMemo.config.JwtService;
import com.example.techMemo.mapper.UserMapper;
import com.example.techMemo.user.Role;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
        var user = User.builder()
                       .name(request.name())
                       .email(request.email())
                       .password(passwordEncoder.encode(request.password()))
                       .role(Role.USER)
                       .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        // 🔥 refreshTokenをcookieへ
        setCookieFromRefreshToken(jwtToken.refreshToken(), response);


        return new AuthenticationResponse(jwtToken.token(), userMapper.toUserResponse(user));

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = repository.findByEmail(request.email())
                             .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        var jwtToken = jwtService.generateToken(authentication);
        // 🔥 refreshTokenをcookieへ
        setCookieFromRefreshToken(jwtToken.refreshToken(), response);
        // 🔥 refreshTokenをcookieへ
        return new AuthenticationResponse(jwtToken.token(), userMapper.toUserResponse(user));

    }

    private void setCookieFromRefreshToken(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //本番ならtrue, localhostならfalse
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }
}
