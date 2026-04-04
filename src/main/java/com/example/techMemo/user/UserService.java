package com.example.techMemo.user;

import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.UserMapper;
import com.example.techMemo.utils.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponse showMyInfo() {
        User user = getUser();
        return mapper.toUserResponse(user);
    }

    private User getUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
    }

}
