package com.hainguyen.shop.mapper;

import com.hainguyen.shop.dtos.response.CommentResponse;
import com.hainguyen.shop.dtos.response.UserResponse;
import com.hainguyen.shop.models.Comment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentResponse mapToCommentResponse(Comment comment, CommentResponse commentResponse){

        return CommentResponse.builder()
                .content(comment.getContent())
                .userResponse(userMapper.mapToUserResponse(comment.getUser(),new UserResponse()))
                .updatedAt(comment.getUpdatedAt())
                .build();

    }
}
