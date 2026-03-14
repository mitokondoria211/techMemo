package com.example.techMemo.bookmark;

import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.exception.UnauthorizedException;
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

    public BookmarkResponse getById(Long id) {
        User user = getUser();
        Bookmark bookmark = repository.findById(id)
                                      .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));
        return mapper.toResponse(bookmark);
    }

    public BookmarkResponse create(BookmarkRequest request) {
        User user = getUser();
        Bookmark bookmark = mapper.toEntity(request, user);

        return mapper.toResponse(repository.save(bookmark));
    }

    public BookmarkResponse update(Long id, BookmarkRequest request) {
        User user = getUser();
        Bookmark bookmark = repository.findById(id)
                                      .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));
        bookmark.update(request.url(), request.title(), request.memo(), request.sortOrder());

        return mapper.toResponse(repository.save(bookmark));
    }

    public void deleteById(Long id) {
        User user = getUser();
        Bookmark bookmark = repository.findById(id)
                                      .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));
        checkOwner(bookmark, user);
        repository.delete(bookmark);
    }

    private User getUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
    }

    private void checkOwner(Bookmark bookmark, User user) {
        if (!user.equals(bookmark.getUser())) {
            throw new UnauthorizedException("このブックマークを操作する権限はありません");
        }
    }


}
