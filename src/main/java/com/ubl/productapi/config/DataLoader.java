package com.ubl.productapi.config;

import com.ubl.productapi.model.Product;
import com.ubl.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                Product.builder()
                        .name("Keyboard Wireless")
                        .price(250000)
                        .stock(40)
                        .category("Accessories")
                        .build(),
                Product.builder()
                        .name("Mouse Gaming RGB")
                        .price(175000)
                        .stock(60)
                        .category("Accessories")
                        .build(),
                Product.builder()
                        .name("Monitor 24 Inch")
                        .price(1800000)
                        .stock(15)
                        .category("Display")
                        .build(),
                Product.builder()
                        .name("Laptop Lenovo ThinkPad")
                        .price(9500000)
                        .stock(8)
                        .category("Laptop")
                        .build(),
                Product.builder()
                        .name("Headset Bluetooth")
                        .price(320000)
                        .stock(30)
                        .category("Audio")
                        .build()
            );

            productRepository.saveAll(products);
            System.out.println("✅ Sample products inserted successfully!");
        } else {
            System.out.println("ℹ️ Products already exist. Skipping preload.");
        }
    }
}
