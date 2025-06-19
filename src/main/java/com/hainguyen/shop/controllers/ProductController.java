package com.hainguyen.shop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.hainguyen.shop.dtos.response.ProductsResponsePage;
import com.hainguyen.shop.services.IProductRedisService;
import com.hainguyen.shop.services.IProductRedisTrendingService;
import com.hainguyen.shop.services.IProductService;
import com.hainguyen.shop.utils.Constants;
import com.hainguyen.shop.dtos.request.ProductDto;
import com.hainguyen.shop.dtos.response.SuccessResponse;
import com.hainguyen.shop.dtos.response.ProductResponse;
import com.hainguyen.shop.utils.LocalizationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final LocalizationUtils localizationUtils;
    private final IProductRedisService productRedisService;
    private final IProductRedisTrendingService productRedisTrendingService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductDto productDto) {

        ProductResponse productResponse = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productResponse);
    }

    @PostMapping(value = "uploads/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> UploadImages(@RequestParam("files") List<MultipartFile> files,
                                                        @PathVariable Long productId) throws IOException {

        productService.uploadProductImage(files, productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse(Constants.STATUS_201,
                        localizationUtils.getLocalizedMessage(Constants.MESSAGE_201)));
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewProductImage(@PathVariable String imageName) {

        Path imagePath = Paths.get("uploads/" + imageName);
        // uploads/0ca19870-50b7-4719-82ae-b6b8272d45cd_062
        try {
            UrlResource resource = new UrlResource(imagePath.toUri());
            // Resource base on Url, Uri: rootFile/imagePath
            if (resource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<ProductsResponsePage> getProducts(@RequestParam(defaultValue = "1") int pageNumber,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "") String keyword,
                                                            @RequestParam(defaultValue = "0") Long categoryId)
            throws JsonProcessingException {

        PageRequest pageRequest = PageRequest.of(
                pageNumber - 1, pageSize, Sort.by("id").ascending()
        );

        ProductsResponsePage productsPerPage = productRedisService.getProductsPerPage(pageRequest);
        if (productsPerPage == null) {
            productsPerPage = productService.getAllProducts(pageRequest, pageNumber, keyword, categoryId);
            productRedisService.saveProductsPerPage(productsPerPage, pageRequest);
        }

        return ResponseEntity.ok().body(productsPerPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        ProductResponse existingProductResponse = productService.getProductById(id);

        // addScore into RScoreSortedSet
        productRedisTrendingService.addVisit(existingProductResponse);

        return ResponseEntity.ok().body(existingProductResponse);
    }

    @GetMapping("/cartProductIds")
    public ResponseEntity<List<ProductResponse>> getProductsByCartProductIds(@RequestParam String cartProductIds) {
        // 1,3,5,7
        List<Long> productIds = Arrays.stream(cartProductIds.split(","))
                .map(Long::valueOf)
                .toList();

        List<ProductResponse> productOrders = productService.findProductsByProductIds(productIds);
        return ResponseEntity.ok().body(productOrders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id,
                                           @Valid @RequestBody ProductDto productDto) {

        Boolean isUpdated = productService.updateProduct(id, productDto);
        return localizationUtils.getResponseChangeRecord(isUpdated, Constants.MESSAGE_417_UPDATE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable Long id) {
        Boolean isDeleted = productService.deleteProduct(id);

        return localizationUtils.getResponseChangeRecord(isDeleted, Constants.MESSAGE_417_DELETE);
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> getFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsProduct(productName)) {
                continue;
            }
            ProductDto productDto = ProductDto.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 5_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(2, 5))
                    .build();
            productService.createProduct(productDto);
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }


}
