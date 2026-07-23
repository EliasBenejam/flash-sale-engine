package com.portfolio.flashsale.application;

import com.portfolio.flashsale.domain.Product;
import com.portfolio.flashsale.infrastructure.cache.RedisStockService;
import com.portfolio.flashsale.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FlashSaleService {

    private final ProductRepository productRepository;
    private final RedisStockService redisStockService;

    public FlashSaleService(ProductRepository productRepository, RedisStockService redisStockService) {
        this.productRepository = productRepository;
        this.redisStockService = redisStockService;
    }

    @Transactional
    public String processBuy(Long productId, Long userId) {
        // 1. Intentar decrementar en Redis de forma ultra rápida y atómica
        Long remainingStock = redisStockService.decrementStock(productId);

        if (remainingStock == null || remainingStock < 0) {
            return "Error: ¡Stock agotado para el producto ID " + productId + "!";
        }

        // 2. Si Redis dio luz verde, persistimos el cambio de manera segura en PostgreSQL
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStock(remainingStock.intValue());
            productRepository.save(product);
            
            return "¡Compra exitosa! Usuario " + userId + " compró " + product.getName() + ". Stock restante: " + remainingStock;
        }

        return "Error crítico: Producto no sincronizado.";
    }
}