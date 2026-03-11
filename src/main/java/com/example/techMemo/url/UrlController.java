package com.example.techMemo.url;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UrlController {

    private final UrlService service;

    @GetMapping
    public ResponseEntity<List<UrlResponse>> getMyUrls() {
        return ResponseEntity.ok(service.getMyUrls());
    }

    @PostMapping
    public ResponseEntity<UrlResponse> create(
        @RequestBody @Valid UrlRequest urlRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(urlRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UrlResponse> update(
        @PathVariable Long id,
        @RequestBody @Valid UrlRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{urlId}/attach/{articleId}")
    public ResponseEntity<UrlResponse> attach(
        @PathVariable Long urlId,
        @PathVariable Long articleId
    ) {
        return ResponseEntity.ok(service.attachToArticle(urlId, articleId));
    }

    @PatchMapping("/{urlId}/detach")
    public ResponseEntity<UrlResponse> detach(
        @PathVariable Long urlId
    ) {
        return ResponseEntity.ok(service.detachFromArticle(urlId));
    }
}
