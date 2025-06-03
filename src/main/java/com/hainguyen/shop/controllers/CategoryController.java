package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.response.CategoryResponse;
import com.hainguyen.shop.services.ICategoryService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.dtos.request.CategoryDto;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(Constants.STATUS_201,
                        localizationUtils.getLocalizedMessage("MESSAGE_201")));
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateCategory(@PathVariable Long id,
                                                         @Valid @RequestBody CategoryDto categoryDto) {
        Boolean isUpdated = categoryService.updateCategory(id, categoryDto);
        return localizationUtils.getResponseChangeRecord(isUpdated,"MESSAGE_417_UPDATE");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable Long id) {
        Boolean isDeleted = categoryService.deleteCategory(id);
       return localizationUtils.getResponseChangeRecord(isDeleted,"MESSAGE_417_DELETE");
    }


}
