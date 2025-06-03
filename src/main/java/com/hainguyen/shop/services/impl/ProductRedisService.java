package com.hainguyen.shop.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hainguyen.shop.dtos.response.ProductsResponsePage;
import com.hainguyen.shop.services.IProductRedisService;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ProductRedisService implements IProductRedisService {
    private final RMapCache<String, ProductsResponsePage> rMapCache;

    public ProductRedisService(RedissonClient redissonClient, ObjectMapper redisMapper){
        this.rMapCache = redissonClient.getMapCache(
                "productsPerPage",
                new TypedJsonJacksonCodec(String.class,ProductsResponsePage.class, redisMapper)
        );
    }



    private String getKeyFrom( PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        String sortDirection =
                Objects.requireNonNull(pageRequest.getSort().getOrderFor("id")).getDirection()
                        == Sort.Direction.ASC ? "asc" : "desc";

        return String.format("all_products:%d:%d:%s", pageNumber+1, pageSize, sortDirection);
    }

    @Override
    public ProductsResponsePage getProductsPerPage(PageRequest pageRequest) {

        String key = this.getKeyFrom(pageRequest);

        return rMapCache.get(key);
    }

    @Override
    public void clear() {
        rMapCache.clear();
    }

    @Override
    //save to Redis
    public void saveProductsPerPage(ProductsResponsePage productsResponsePage, PageRequest pageRequest) {

        String key = this.getKeyFrom(pageRequest);
        rMapCache.fastPut(key,productsResponsePage,3600, TimeUnit.SECONDS);
    }
}
