package com.example.techMemo.category;

import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    //一覧取得(全件)
    public List<CategoryResponse> getAll() {
        return repository.findAllByOrderByNameAsc()
                         .stream()
                         .map(mapper::toResponse)
                         .toList();
    }

    //作成
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request) {

        //重複チェック
        if (repository.existsByName((request.name()))) {
            throw new IllegalStateException("同じ名前のカテゴリーが既に存在しています");
        }
        Category category = mapper.toEntity(request);
        Category saved = repository.save(category);
        return mapper.toResponse(category);

    }

    //削除
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("カテゴリーが見つかりません");
        }
        repository.deleteById(id);
    }
}
