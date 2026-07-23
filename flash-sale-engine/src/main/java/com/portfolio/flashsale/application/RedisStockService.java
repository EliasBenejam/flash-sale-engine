package com.portfolio.flashsale.infrastructure.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisStockService {

    private final StringRedisTemplate redisTemplate;

    public RedisStockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getStockKey(Long productId) {
        return "product:" + productId + ":stock";
    }

    // Inicializa o sincroniza el stock en Redis
    public void setStock(Long productId, int stock) {
        redisTemplate.opsForValue().set(getStockKey(productId), String.valueOf(stock));
    }

    /**
     * Decrementa el stock de forma atómica en Redis.
     * @return El stock restante después de decrementar, o -1 si ya estaba agotado.
     */
    public Long decrementStock(Long productId) {
        String key = getStockKey(productId);
        Long remainingStock = redisTemplate.opsForValue().decrement(key);

        if (remainingStock != null && remainingStock < 0) {
            // Rollback inmediato si nos pasamos del stock (overselling prevention)
            redisTemplate.opsForValue().increment(key);
            return -1L;
        }

        return remainingStock;
    }
}