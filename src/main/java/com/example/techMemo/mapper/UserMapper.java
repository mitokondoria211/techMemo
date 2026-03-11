package com.example.techMemo.mapper;

import com.example.techMemo.user.User;
import com.example.techMemo.user.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
