package com.ubl.productapi.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(
	    name = "products",
	    indexes = {
	        @Index(name = "idx_product_name", columnList = "name"),
	        @Index(name = "idx_product_category", columnList = "category_id"),
	        @Index(name = "idx_product_price", columnList = "price")
	    }
	)
@NamedQueries({
    @NamedQuery(
        name = "Product.searchByMinPrice",
        query = "SELECT p FROM Product p WHERE p.price >= :minPrice"
    )
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private double price;

    private int stock;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;
}