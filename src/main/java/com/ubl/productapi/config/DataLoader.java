package com.ubl.productapi.config;

import com.ubl.productapi.model.Product;
import com.ubl.productapi.model.Category;
import com.ubl.productapi.repository.ProductRepository;
import com.ubl.productapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            // create categories
            Category accessories = Category.builder().name("Accessories").build();
            Category display = Category.builder().name("Display").build();
            Category laptop = Category.builder().name("Laptop").build();
            Category audio = Category.builder().name("Audio").build();

            List<Category> savedCats = categoryRepository.saveAll(List.of(accessories, display, laptop, audio));
            // map names to saved instances
            Category acc = savedCats.stream().filter(c -> "Accessories".equals(c.getName())).findFirst().orElse(accessories);
            Category disp = savedCats.stream().filter(c -> "Display".equals(c.getName())).findFirst().orElse(display);
            Category lap = savedCats.stream().filter(c -> "Laptop".equals(c.getName())).findFirst().orElse(laptop);
            Category aud = savedCats.stream().filter(c -> "Audio".equals(c.getName())).findFirst().orElse(audio);

            List<Product> products = List.of(
                Product.builder()
                        .name("Keyboard Wireless")
                        .price(250000)
                        .stock(40)
                        .category(acc)
                        .build(),
                Product.builder()
                        .name("Mouse Gaming RGB")
                        .price(175000)
                        .stock(60)
                        .category(acc)
                        .build(),
                Product.builder()
                        .name("Monitor 24 Inch")
                        .price(1800000)
                        .stock(15)
                        .category(disp)
                        .build(),
                Product.builder()
                        .name("Laptop Lenovo ThinkPad")
                        .price(9500000)
                        .stock(8)
                        .category(lap)
                        .build(),
                Product.builder()
                        .name("Headset Bluetooth")
                        .price(320000)
                        .stock(30)
                        .category(aud)
                        .build()
            );

            productRepository.saveAll(products);
            System.out.println("✅ Sample products inserted successfully!");
        } else {
            System.out.println("ℹ️ Products already exist. Skipping preload.");
        }
    }
}