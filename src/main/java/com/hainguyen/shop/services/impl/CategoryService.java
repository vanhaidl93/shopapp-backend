package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.dtos.request.CategoryDto;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.models.Category;
import com.hainguyen.shop.repositories.CategoryRepo;
import com.hainguyen.shop.dtos.response.CategoryResponse;
import com.hainguyen.shop.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @Override
    public void createCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();
        categoryRepo.save(category);
    }

    @Override
    public List<CategoryResponse> getCategoriesPerPage(Pageable pageable) {
        Page<Category> categoriesPage = categoryRepo.findAll(pageable);
        List<Category> categories = categoriesPage.getContent();

        return categories.stream()
                .map(category ->modelMapper.map(category,CategoryResponse.class))
                .toList();
    }


    @Override
    @Transactional
    public Boolean updateCategory(Long id, CategoryDto categoryDto) {

        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString()));
        existingCategory.setName(categoryDto.getName());

        return true;
    }

    @Override
    public Boolean deleteCategory(Long id) {

        categoryRepo.deleteById(id);
        return true;
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId.toString()));

        return CategoryResponse.builder()
                .id(existingCategory.getId())
                .name(existingCategory.getName())
                .build();
    }
}
