package com.hainguyen.shop.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotNull(message = "ProductId is required")
    private Long productId;
    @NotNull(message = "ProductId is required")
    private Long userId;
    @NotBlank(message = "Content can't be blank ")
    private String content;
}
