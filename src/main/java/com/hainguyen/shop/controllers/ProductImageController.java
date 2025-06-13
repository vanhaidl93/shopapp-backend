package com.hainguyen.shop.controllers;

import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.models.ProductImage;
import com.hainguyen.shop.services.IProductImageService;
import com.hainguyen.shop.services.impl.ProductService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/productImages")
@RequiredArgsConstructor
public class ProductImageController {

    private final IProductImageService productImageService;
    private final ProductService productService;
    private final LocalizationUtils localizationUtils;

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> delete(@PathVariable Long id) {

        // ProductImage database
        ProductImage deleteProductImage = productImageService.deleteProductImage(id);
        // uploads folder
        productService.deleteFile(deleteProductImage.getImageName());

        return ResponseEntity.ok(new SuccessResponse(Constants.STATUS_200,
                localizationUtils.getLocalizedMessage(Constants.MESSAGE_200)));

    }
}
