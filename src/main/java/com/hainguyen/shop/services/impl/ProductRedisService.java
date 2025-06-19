package com.hainguyen.shop.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hainguyen.shop.dtos.response.ProductsResponsePage;
import com.hainguyen.shop.services.IProductRedisService;
import org.redisson.api.BatchOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ProductRedisService implements IProductRedisService {

    private final RLocalCachedMap<String,ProductsResponsePage> rLocalCachedMap;

    public ProductRedisService(RedissonClient redissonClient, ObjectMapper redisMapper){
        this.rLocalCachedMap = redissonClient.getLocalCachedMap(
                LocalCachedMapOptions .<String, ProductsResponsePage>name("products:page")
                        .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                        .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR)
                        .codec(new TypedJsonJacksonCodec(String.class, ProductsResponsePage.class,redisMapper))
        );
    }



    @Override
    public ProductsResponsePage getProductsPerPage(PageRequest pageRequest) {

        String key = this.getKeyFrom(pageRequest);
        return rLocalCachedMap.get(key);
    }

    @Override
    public void clear() {
        rLocalCachedMap.clear();
    }

    @Override
    //save to Redis
    public void saveProductsPerPage(ProductsResponsePage productsResponsePage, PageRequest pageRequest) {

        String key = this.getKeyFrom(pageRequest);
        rLocalCachedMap.fastPutAsync(key,productsResponsePage);
    }

    private String getKeyFrom( PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        String sortDirection =
                Objects.requireNonNull(pageRequest.getSort().getOrderFor("id")).getDirection()
                        == Sort.Direction.ASC ? "asc" : "desc";

        return String.format("all_products:%d:%d:%s", pageNumber+1, pageSize, sortDirection);
    }
}
