package com.example.techMemo.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
   private final CategoryService service;

   @GetMapping
   public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
   }

   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<CategoryResponse> create(
       @RequestBody @Valid CategoryCreateRequest request
   ) {
       return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<CategoryResponse> delete(@PathVariable long id) {
       service.delete(id);
       return ResponseEntity.noContent().build();
   }
}
