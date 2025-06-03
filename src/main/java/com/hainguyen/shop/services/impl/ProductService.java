package com.hainguyen.shop.services.impl;

import com.hainguyen.shop.dtos.request.ProductDto;
import com.hainguyen.shop.exceptions.InvalidParamException;
import com.hainguyen.shop.exceptions.ResourceNotFoundException;
import com.hainguyen.shop.mapper.ProductMapper;
import com.hainguyen.shop.models.Category;
import com.hainguyen.shop.models.Product;
import com.hainguyen.shop.models.ProductImage;
import com.hainguyen.shop.repositories.CategoryRepo;
import com.hainguyen.shop.repositories.ProductImageRepo;

import com.hainguyen.shop.repositories.ProductRepo;
import com.hainguyen.shop.dtos.response.ProductResponse;
import com.hainguyen.shop.dtos.response.ProductsResponsePage;
import com.hainguyen.shop.services.IProductRedisService;
import com.hainguyen.shop.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductImageRepo productImageRepo;
    private final ProductMapper productMapper;
    private final IProductRedisService productRedisService;

    @Override
    public void createProduct(ProductDto productDto) {
        Category existingCategory = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDto.getCategoryId().toString()));
        Product newProduct = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .thumbnail(productDto.getThumbnail())
                .description(productDto.getDescription())
                .category(existingCategory).
                build();

        productRepo.save(newProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));

        return productMapper.mapToProductResponse(existingProduct, new ProductResponse());
    }

    @Override
    public ProductsResponsePage getAllProducts(PageRequest pageRequest, int pageNumber,
                                               String keyword, Long categoryId) {

        Page<Product> productPages = productRepo.searchProducts(categoryId, keyword, pageRequest);
        int totalPages = productPages.getTotalPages();

        List<ProductResponse> productResponsesPerPage =
                productPages.getContent().stream()
                .map(product -> productMapper.mapToProductResponse(product, new ProductResponse()))
                .toList();
        ;

        return new ProductsResponsePage(productResponsesPerPage, pageNumber, totalPages);
    }

    @Override
    @Transactional
    public Boolean updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
        Category existingCategory = categoryRepo.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDto.getCategoryId().toString()));

        existingProduct.setName(productDto.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setThumbnail(productDto.getThumbnail());

        return true;
    }

    @Override
    public Boolean deleteProduct(Long id) {

        productRepo.deleteById(id);

        return true;

    }

    @Override
    public boolean existsProduct(String name) {
        return productRepo.existsByName(name);
    }

    @Override
    public List<ProductResponse> findProductsByProductIds(List<Long> productIds) {

        List<Product> products = productRepo.findAllById(productIds);

        return products.stream()
                .map(product -> productMapper.mapToProductResponse(product, new ProductResponse()))
                .toList();
    }

    @Override
    public void uploadProductImage(List<MultipartFile> files, Long productId)
            throws IOException {

        for (MultipartFile file : files) {
            if (file.getSize() == 0) continue;

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new RuntimeException("File too large! Maximum size is 10MB!");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("File must be an image!");
            }
            // store file into uploads folder.
            String uploadedUniqueFileName = storeImage(file);

            // save thumbnail of uploaded file into product_images table.
            createProductImage(uploadedUniqueFileName, productId);
        }
    }

    // save only 5 files into product_images by productId
    private void createProductImage(String uploadUniqueFileName, Long productId) {

        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId.toString()));

        // restrict insert over 5 images.
        int size = productImageRepo.findByProductId(productId).size();
        if (size >= 5) {
            throw new InvalidParamException("Number of images exceeded 5.");
        }

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageName(uploadUniqueFileName)
                .build();

        productImageRepo.save(newProductImage);
    }

    // Store files into uploads folder.
    private String storeImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // create unique fileName ( add UUID random), avoid to overwrite.
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        // create uploads folder at current working directory
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        // copy uploaded file into uploads directory.
        Path uniqueFilePath = uploadDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), uniqueFilePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }


}


