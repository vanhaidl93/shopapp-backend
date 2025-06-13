package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    private String content;
    private UserResponse userResponse;
    private LocalDateTime updatedAt;

}
