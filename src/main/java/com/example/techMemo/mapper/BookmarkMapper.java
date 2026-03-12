package com.example.techMemo.mapper;

import com.example.techMemo.bookmark.Bookmark;
import com.example.techMemo.bookmark.BookmarkCreateRequest;
import com.example.techMemo.bookmark.BookmarkResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class})
public interface BookmarkMapper {

    BookmarkResponse toResponse(Bookmark bookmark);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Bookmark toEntity(BookmarkCreateRequest request);

    List<BookmarkResponse> toResponseList(List<Bookmark> bookmarks);


}
