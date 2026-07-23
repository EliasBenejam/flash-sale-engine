package com.portfolio.flashsale;

import com.portfolio.flashsale.domain.Product;
import com.portfolio.flashsale.infrastructure.persistence.ProductRepository;
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

    // Bean para poblar la base de datos de pruebas automáticamente
    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Product("Zapatillas Nike Jordan", 10, 150.00));
                System.out.println("✅ Producto de prueba insertado en PostgreSQL");
            }
        };
    }
}