package com.portfolio.flashsale.infrastructure.controller;

import com.portfolio.flashsale.application.FlashSaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flash-sale")
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    // Inyección de dependencias
    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyProduct(@RequestParam Long productId, @RequestParam Long userId) {
        // Delegamos toda la lógica pesada al servicio
        String response = flashSaleService.processBuy(productId, userId);
        
        if (response.startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}