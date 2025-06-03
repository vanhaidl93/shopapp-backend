package com.hainguyen.shop.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class CategoryDto {

    @NotEmpty(message = "Category's name cannot be empty" )
    private String name;
}
