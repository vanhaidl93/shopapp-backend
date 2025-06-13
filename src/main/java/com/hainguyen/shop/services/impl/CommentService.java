package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.dtos.request.CommentDto;
import com.hainguyen.shop.dtos.response.CommentResponse;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.mapper.CommentMapper;
import com.hainguyen.shop.models.Comment;
import com.hainguyen.shop.models.Product;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.repositories.CommentRepo;
import com.hainguyen.shop.repositories.ProductRepo;
import com.hainguyen.shop.repositories.UserRepo;
import com.hainguyen.shop.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse insertComment(CommentDto commentDto) {
        User existingUser = userRepo.findById(commentDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User","userId",commentDto.getUserId().toString()));
        Product existingProduct = productRepo.findById(commentDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",commentDto.getProductId().toString()));

        Comment newComment = Comment.builder()
                .content(commentDto.getContent())
                .user(existingUser)
                .product(existingProduct)
                .build();
        return commentMapper.mapToCommentResponse(commentRepo.save(newComment),new CommentResponse());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

    @Override
    @Transactional
    public void updateComment(Long id, CommentDto commentDto) {
        Comment existingComment = commentRepo.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Comment","commentId",id.toString()));

        existingComment.setContent(commentDto.getContent());
    }

    @Override
    public List<CommentResponse> getCommentsByUserAndProduct(Long userId, Long productId) {
        List<Comment> comments = commentRepo.findByUserIdAndProductId(userId, productId);

        return comments.stream()
                .map(comment -> commentMapper.mapToCommentResponse(comment,new CommentResponse()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByProduct(Long productId) {
        List<Comment> comments = commentRepo.findByProductId(productId);
        return comments.stream()
                .map(comment -> commentMapper.mapToCommentResponse(comment,new CommentResponse()))
                .collect(Collectors.toList());
    }
}
