package com.example.techMemo.mapper;

import com.example.techMemo.tag.Tag;
import com.example.techMemo.tag.TagResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);
    List<TagResponse> toResponseList(List<Tag> tags);
}
