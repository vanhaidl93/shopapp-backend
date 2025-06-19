package com.hainguyen.shop.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hainguyen.shop.dtos.response.ProductResponse;
import com.hainguyen.shop.services.IProductRedisTrendingService;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductRedisTrendingService implements IProductRedisTrendingService {

    private final RScoredSortedSet<ProductResponse> sortedSet;

    public ProductRedisTrendingService(RedissonClient client, ObjectMapper redisMapper){

        String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDateTime.now());
        sortedSet = client.getScoredSortedSet("product:visit:" + format,
                                             new TypedJsonJacksonCodec(ProductResponse.class, redisMapper));
    }


    @Override
    public Map<Double,ProductResponse> topNProducts(int n) {
        return sortedSet.entryRangeReversed(0, n - 1).stream()
                .collect(Collectors.toMap(
                                ScoredEntry::getScore,
                                ScoredEntry::getValue,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new
                        )
                );
    }

    @Override
    public void addVisit(ProductResponse productResponse) {
        sortedSet.addScore(productResponse,1);
    }

}
