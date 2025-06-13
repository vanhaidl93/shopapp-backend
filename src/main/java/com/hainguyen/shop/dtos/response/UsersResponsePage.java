package com.hainguyen.shop.dtos.response;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsersResponsePage {

    private List<UserResponse> userResponses;
    private int currentPage;
    private int totalPages;
}
