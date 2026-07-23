package com.portfolio.flashsale;

import com.portfolio.flashsale.domain.Product;
import com.portfolio.flashsale.infrastructure.persistence.ProductRepository;
import com.portfolio.flashsale.infrastructure.cache.RedisStockService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@SpringBootApplication
public class FlashSaleEngineApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(FlashSaleEngineApplication.class, args);
    }

    @Bean
    CommandLineRunner initRedisStock(ProductRepository productRepository, RedisStockService redisStockService) {
        return args -> {
            // Buscamos o creamos el producto de prueba para asegurar un stock inicial de 100 unidades
            Product product = productRepository.findById(1L).orElseGet(() -> {
                Product newProduct = new Product();
                newProduct.setName("Smartphone Flash Sale");
                newProduct.setPrice(299.99);
                return newProduct;
            });

            // Forzamos el stock a 100 para las pruebas del motor de alta concurrencia
            product.setStock(100);
            productRepository.save(product);
            System.out.println("Producto de prueba inicializado/actualizado en PostgreSQL con 100 unidades.");

            // Sincronizamos el stock exacto hacia Redis
            redisStockService.setStock(product.getId(), product.getStock());
            System.out.println("Stock sincronizado en Redis para el producto ID: " + product.getId() + " con " + product.getStock() + " unidades.");
        };
    }
}