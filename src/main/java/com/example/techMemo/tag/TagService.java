package com.example.techMemo.tag;

import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.TagMapper;
import com.example.techMemo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;
    private final UserRepository userRepository;
    private final TagMapper mapper;
    @Transactional(readOnly = true)
    //タグ全取得
    public List<TagResponse> findAll() {
        return repository.findAllByOrderByNameAsc()
            .stream()
            .map(mapper::toResponse)
            .toList();
    }

    @Transactional
    public Tag findOrCreate(String name) {
        return repository.findByName(name)
                            .orElseGet(() -> {
                                try {
                                    return repository.save(new Tag(name));
                                } catch (DataIntegrityViolationException e) {
                                    return repository.findByName(name)
                                                        .orElseThrow();
                                }
                            });
    }

    @Transactional
    public List<Tag> findOrCreateAll(List<String> tagNames) {
        return tagNames.stream()
                       .map(this::findOrCreate)
                       .toList();
    }

    //タグの作成
    public TagResponse create(TagRequest request) {
        if (repository.existsByName((request.name()))) {
            throw new IllegalStateException("同じ名前のタグが既に存在します");
        }

        Tag tag = Tag.builder().name(request.name()).build();
        return mapper.toResponse(repository.save(tag));
    }

    //タグ削除
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("タグが見つかりません");
        }
        repository.deleteById(id);
    }

}
