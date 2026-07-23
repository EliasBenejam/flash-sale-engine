package com.portfolio.flashsale.infrastructure.web;

import com.portfolio.flashsale.application.FlashSaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flash-sale")
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @PostMapping("/buy/{productId}")
    public ResponseEntity<String> buyProduct(
            @PathVariable Long productId, 
            @RequestParam Long userId) {
        
        String response = flashSaleService.processBuy(productId, userId);
        
        if (response.startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}