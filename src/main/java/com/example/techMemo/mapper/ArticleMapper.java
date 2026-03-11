package com.example.techMemo.mapper;

import com.example.techMemo.article.dto.ArticleCreateRequest;
import com.example.techMemo.article.dto.ArticleDetailResponse;
import com.example.techMemo.article.dto.ArticleResponse;
import com.example.techMemo.article.entity.Article;

import com.example.techMemo.category.Category;
import com.example.techMemo.tag.Tag;
import com.example.techMemo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {TagMapper.class, UrlMapper.class, UserMapper.class, CategoryMapper.class})
public interface ArticleMapper {

    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "likedByMe" , ignore = true)
    ArticleResponse toResponse(Article article);

    @Mapping(target = "likedByMe", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    ArticleDetailResponse toResponseDetail(Article article);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "urls", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    Article toEntity(ArticleCreateRequest request, User user, Category category, List<Tag> tags);

    // likeCount・likedByMeをセットするための変換
    default ArticleResponse toResponse(Article article, long likeCount, boolean likedByMe) {
        ArticleResponse response = toResponse(article);
        return ArticleResponse.builder()
                              .id(response.id())
                              .title(response.title())
                              .content(response.content())
                              .publicFlag(response.publicFlag())
                              .user(response.user())
                              .category(response.category())
                              .tags(response.tags())
                              .likeCount(likeCount)
                              .likedByMe(likedByMe)
                              .createdAt(response.createdAt())
                              .updatedAt(response.updatedAt())
                              .build();
    }
    default ArticleDetailResponse toResponseDetail(Article article, long likeCount, boolean likedByMe) {
        ArticleDetailResponse response = toResponseDetail(article);
        return ArticleDetailResponse.builder()
                              .id(response.id())
                              .title(response.title())
                              .content(response.content())
                              .publicFlag(response.publicFlag())
                              .user(response.user())
                              .category(response.category())
                              .tags(response.tags())
            .urls(response.urls())
                              .likeCount(likeCount)
                              .likedByMe(likedByMe)
                              .createdAt(response.createdAt())
                              .updatedAt(response.updatedAt())
                              .build();
    }

    List<ArticleResponse> toResponseList(List<Article> articles);


}
