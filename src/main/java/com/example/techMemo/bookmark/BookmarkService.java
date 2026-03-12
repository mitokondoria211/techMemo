package com.example.techMemo.bookmark;

import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.BookmarkMapper;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import com.example.techMemo.utils.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository repository;
    private final UserRepository userRepository;
    private final BookmarkMapper mapper;

    public List<BookmarkResponse> getMyAll() {
        User user = getUser();
        List<Bookmark> bookmarks = repository.findByUser(user);
        return mapper.toResponseList(bookmarks);
    }

    private User getUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
    }


}
