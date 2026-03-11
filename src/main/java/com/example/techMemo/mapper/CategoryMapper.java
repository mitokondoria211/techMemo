package com.example.techMemo.mapper;

import com.example.techMemo.category.Category;
import com.example.techMemo.category.CategoryCreateRequest;
import com.example.techMemo.category.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);


    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryCreateRequest request);
}
