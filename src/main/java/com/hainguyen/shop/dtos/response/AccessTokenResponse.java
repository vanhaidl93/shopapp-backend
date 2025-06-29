package com.hainguyen.shop.dtos.response;

import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken) {
}
