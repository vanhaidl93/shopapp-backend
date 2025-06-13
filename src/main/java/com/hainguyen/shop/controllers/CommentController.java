package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.request.CommentDto;
import com.hainguyen.shop.dtos.response.CommentResponse;
import com.hainguyen.shop.models.User;
import com.hainguyen.shop.services.ICommentService;
import com.hainguyen.shop.services.IUserService;
import com.hainguyen.shop.services.impl.CommentService;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;
    private final LocalizationUtils localizationUtils;
    private final IUserService userService;

    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam("productId") Long productId) {

        List<CommentResponse> commentResponses;
        if (userId == null) {
            commentResponses = commentService.getCommentsByProduct(productId);
        } else {
            commentResponses = commentService.getCommentsByUserAndProduct(userId, productId);
        }
        return ResponseEntity.ok(commentResponses);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> insertComment(@Valid @RequestBody CommentDto commentDto) {

        // Insert the new comment
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userService.getUserByPhoneNumber(authentication.getName());

        if (!Objects.equals(authenticatedUser.getId(), commentDto.getUserId())) {
            return ResponseEntity.badRequest().body("You cannot comment as another user");
        }

        CommentResponse commentResponse= commentService.insertComment(commentDto);

        return ResponseEntity.ok(commentResponse);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> updateComment(@PathVariable("id") Long commentId,
                                           @Valid @RequestBody CommentDto commentDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = userService.getUserByPhoneNumber(authentication.getName());

        if (!Objects.equals(authenticatedUser.getId(), commentDto.getUserId())) {
            return ResponseEntity.badRequest().body("You cannot update another user's comment");
        }

        commentService.updateComment(commentId, commentDto);
        return ResponseEntity.ok(localizationUtils.getLocalizedMessage("MESSAGE_200"));

    }


}
