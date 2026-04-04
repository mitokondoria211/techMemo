package com.example.techMemo.mapper;

import com.example.techMemo.url.Url;
import com.example.techMemo.url.UrlRequest;
import com.example.techMemo.url.UrlResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UrlMapper {
    @Mapping(target = "articleId", source = "article.id")  // article_idも返す
    @Mapping(target = "articleTitle", source = "article.title")
    UrlResponse toResponse(Url url);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "article", ignore = true)     // Serviceでセット
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Url toEntity(UrlRequest request);

    List<UrlResponse> toResponseList(List<Url> urls);
}
