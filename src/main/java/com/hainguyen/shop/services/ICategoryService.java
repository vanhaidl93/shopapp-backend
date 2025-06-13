package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.CategoryDto;
import com.hainguyen.shop.dtos.response.CategoryResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    void createCategory(CategoryDto categoryDto);
    List<CategoryResponse> getCategoriesPerPage(Pageable pageable);
    Boolean updateCategory(Long id, CategoryDto categoryDto);
    Boolean deleteCategory(Long id);


}
