package com.ubl.productapi.specification;

import com.ubl.productapi.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpec {

	// ==============================================================
	// 1. Filter berdasarkan nama produk (LIKE %keyword%)
	// ==============================================================
	public static Specification<Product> nameContains(String keyword) {
		return (root, query, cb) -> {
			if (keyword == null || keyword.isBlank())
				return null;

			return cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
		};
	}

	// ==============================================================
	// 2. Filter berdasarkan nama kategori (JOIN Category)
	// ==============================================================
	public static Specification<Product> hasCategory(String categoryName) {
		return (root, query, cb) -> {
			if (categoryName == null || categoryName.isBlank())
				return null;

			// JOIN category untuk akses category.name
			return cb.equal(cb.lower(root.join("category").get("name")), categoryName.toLowerCase());
		};
	}

	// ==============================================================
	// 3. Filter berdasarkan rentang harga (min-max)
	// ==============================================================
	public static Specification<Product> priceBetween(Double min, Double max) {
		return (root, query, cb) -> {
			if (min == null && max == null)
				return null;

			if (min != null && max != null) {
				return cb.between(root.get("price"), min, max);
			}

			if (min != null) {
				return cb.greaterThanOrEqualTo(root.get("price"), min);
			}

			return cb.lessThanOrEqualTo(root.get("price"), max);
		};
	}

	// ==============================================================
	// 4. Filter kategori ID (jika ID dipakai untuk filtering)
	// ==============================================================
	public static Specification<Product> categoryIdEquals(Long categoryId) {
		return (root, query, cb) -> {
			if (categoryId == null)
				return null;

			return cb.equal(root.join("category").get("id"), categoryId);
		};
	}
}
