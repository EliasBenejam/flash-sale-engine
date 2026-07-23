package com.portfolio.flashsale.application;

import com.portfolio.flashsale.domain.Product;
import com.portfolio.flashsale.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FlashSaleService {

    private final ProductRepository productRepository;

    public FlashSaleService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public String processBuy(Long productId, Long userId) {
        // Usamos el método con Lock Pesimista
        Optional<Product> productOpt = productRepository.findByIdForUpdate(productId);

        if (productOpt.isEmpty()) {
            return "Error: Producto con ID " + productId + " no encontrado.";
        }

        Product product = productOpt.get();

        if (product.getStock() <= 0) {
            return "Error: ¡Stock agotado para el producto " + product.getName() + "!";
        }

        product.setStock(product.getStock() - 1);
        productRepository.save(product);

        return "¡Compra exitosa! Usuario " + userId + " compró " + product.getName() + ". Stock restante: " + product.getStock();
    }
}