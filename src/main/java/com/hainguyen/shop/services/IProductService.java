package com.hainguyen.shop.services;

import com.hainguyen.shop.dtos.request.ProductDto;
import com.hainguyen.shop.dtos.response.ProductResponse;
import com.hainguyen.shop.dtos.response.ProductsResponsePage;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {

    ProductResponse createProduct(ProductDto productDto);
    ProductResponse getProductById(Long id);
    ProductsResponsePage getAllProducts(PageRequest pageRequest, int pageNumber, String keyword, Long categoryId);
    Boolean updateProduct(Long id, ProductDto productDto);
    Boolean deleteProduct(Long id);
    boolean existsProduct(String name);

    void uploadProductImage(List<MultipartFile> files, Long productId) throws IOException;

    List<ProductResponse> findProductsByProductIds(List<Long> productIds);
    void deleteFile(String filename);
}
