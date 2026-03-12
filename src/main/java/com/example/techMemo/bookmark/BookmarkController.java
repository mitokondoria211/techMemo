package com.example.techMemo.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService service;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookmarkResponse>> getMyAll() {
        return ResponseEntity.ok(service.getMyAll());
    }
}
