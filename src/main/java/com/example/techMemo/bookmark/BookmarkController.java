package com.example.techMemo.bookmark;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getMyAllCount() {
        return ResponseEntity.ok(service.getMyBookmarksCount());
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkResponse> getBookmarkById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkResponse> create(@RequestBody @Valid BookmarkRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookmarkResponse> update(@PathVariable Long id,
                                                   @RequestBody @Valid BookmarkRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
