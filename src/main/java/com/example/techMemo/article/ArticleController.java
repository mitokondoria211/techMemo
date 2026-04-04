package com.example.techMemo.article;

import com.example.techMemo.article.dto.*;
import com.example.techMemo.like.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService service;
    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> getArticles(Pageable pageable) {
        return ResponseEntity.ok(service.getArticles(pageable));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ArticleResponse>> getMyArticles(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long tagId,
        @RequestParam(required = false) Long categoryId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(service.getMyArticles(keyword, tagId, categoryId, pageable));
    }

    @GetMapping("/me/recent")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ArticleResponse>> getMyArticles(
    ) {
        return ResponseEntity.ok(service.getRecentMyArticles());
    }


    @GetMapping("/me/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getMyArticlesCount() {
        return ResponseEntity.ok(service.getMyArticlesCount());
    }

    @GetMapping("/me/count/private")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getMyArticlesCountAndPrivate() {
        return ResponseEntity.ok(service.getMyArticlesCountAndPrivate());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArticleDetailResponse> getArticle(@PathVariable Long id) {
        return ResponseEntity.ok(service.getArticle(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleCreateRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleResponse> update(
        @PathVariable Long id, @Valid @RequestBody ArticleUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/visibility")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleResponse> updateVisibility(
        @PathVariable Long id,
        @Valid @RequestBody ArticleUpdatePublicFlagRequest request
    ) {
        service.updateVisibility(id, request.publicFlag());
        return ResponseEntity.ok(service.updateVisibility(id, request.publicFlag()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ArticleResponse>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long tagId,
        @RequestParam(required = false) Long categoryId,
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.search(keyword, tagId, categoryId, pageable));
    }

    @PostMapping("/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> like(
        @PathVariable Long id
    ) {
        likeService.like(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlike(
        @PathVariable Long id) {
        likeService.unlike(id);
        return ResponseEntity.noContent().build();
    }


}
