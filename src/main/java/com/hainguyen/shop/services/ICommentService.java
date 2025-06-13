package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.CommentDto;
import com.hainguyen.shop.dtos.response.CommentResponse;

import java.util.List;

public interface ICommentService {

    CommentResponse insertComment(CommentDto comment);

    void deleteComment(Long commentId);

    void updateComment(Long id, CommentDto commentDto);

    List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId);

    List<CommentResponse> getCommentsByProduct(Long productId);
}
