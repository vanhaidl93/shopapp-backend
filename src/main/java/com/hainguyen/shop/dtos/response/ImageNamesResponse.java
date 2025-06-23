package com.hainguyen.shop.dtos.response;


import lombok.Builder;

import java.util.List;

@Builder
public record ImageNamesResponse(List<String> imageNames) {
}
